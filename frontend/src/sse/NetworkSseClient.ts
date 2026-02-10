import type { EventLogEntry, EventType } from "../models/EventLogEntry";
import type { EventLogAction } from "../state/EventLogAction";
import type { NetworkAction } from "../state/NetworkAction";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

export class NetworkSseClient {
  private eventSource?: EventSource;

  private networkDispatch?: React.Dispatch<NetworkAction>;
  private eventLogDispatch?: React.Dispatch<EventLogAction>;

  async connect(
    rootDeviceId: number,
    networkDispatch: React.Dispatch<NetworkAction>,
    eventLogDispatch: React.Dispatch<EventLogAction>,
  ) {
    this.networkDispatch = networkDispatch;
    this.eventLogDispatch = eventLogDispatch;

    try {
      const topology = await this.fetchTopology();
      this.networkDispatch({
        type: "INITIAL_STATE_RECEIVED",
        payload: {
          rootDeviceId: rootDeviceId,
          deviceIds: topology.devices.map((d) => d.id),
          edges: topology.edges.map((e) => ({ from: e.from, to: e.to })),
        },
      });

      console.log("CONNECTION_STATUS", "Initial topology loaded");
    } catch (error) {
      console.log("CONNECTION_STATUS", `Failed to load topology: ${error}`);
      throw error;
    }

    this.eventSource = new EventSource(
      `${API_BASE_URL}/devices/${rootDeviceId}/reachable-devices`,
    );

    this.eventSource.addEventListener("Initial State", (event) =>
      this.handleInitialState(JSON.parse((event as MessageEvent).data)),
    );
    this.eventSource.addEventListener("Added Device", (event) =>
      this.handleAddedDevice(JSON.parse((event as MessageEvent).data)),
    );
    this.eventSource.addEventListener("Removed Device", (event) =>
      this.handleRemovedDevice(JSON.parse((event as MessageEvent).data)),
    );
    this.eventSource.onerror = (event) => this.handleError(event);
  }

  private async fetchTopology(): Promise<{
    devices: Array<{ id: number; name: string }>;
    edges: Array<{ from: number; to: number }>;
  }> {
    const response = await fetch(`${API_BASE_URL}/network/topology`);
    if (!response.ok) {
      throw new Error(`Failed to fetch topology: ${response.statusText}`);
    }
    return response.json();
  }

  disconnect() {
    this.eventSource?.close();
    this.eventSource = undefined;
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  private handleError(_: Event) {
    this.log("CONNECTION_STATUS", "SSE disconnected");
    this.disconnect();
  }

  private handleInitialState(data: {
    type: "INITIAL_STATE";
    deviceIds: number[];
  }) {
    this.log("SSE_UPDATE", `Initial State: ${JSON.stringify(data, null, 2)}`);
  }

  private handleAddedDevice(data: { type: "ADDED"; deviceId: number }) {
    this.networkDispatch?.({
      type: "DEVICE_STATE_CHANGED",
      payload: { deviceId: data.deviceId, visible: true },
    });

    this.log("SSE_UPDATE", JSON.stringify(data, null, 2));
  }

  private handleRemovedDevice(data: { type: "REMOVED"; deviceId: number }) {
    this.networkDispatch?.({
      type: "DEVICE_STATE_CHANGED",
      payload: { deviceId: data.deviceId, visible: false },
    });

    this.log("SSE_UPDATE", JSON.stringify(data, null, 2));
  }

  private log(type: EventType, message: string) {
    if (!this.eventLogDispatch) return;

    const entry: EventLogEntry = {
      id: crypto.randomUUID(),
      timestamp: Date.now(),
      type,
      message,
    };

    this.eventLogDispatch({
      type: "ADD_EVENT",
      payload: entry,
    });
  }
}
