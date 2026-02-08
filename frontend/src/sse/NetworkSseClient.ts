import type { DeviceStateChangeDTO } from "../dtos/DeviceStateChangeDTO";
import type { InitialStateDTO } from "../dtos/InitialStateDTO";
import type { SseEventDTO } from "../dtos/SseEventDTO";
import { eventLogStore } from "../state/eventLogStore";
import { networkStore } from "../state/networkStore";

export class NetworkSseClient {
  private source: EventSource | null = null;
  private readonly rootDeviceId: number;

  constructor(rootDeviceId: number) {
    this.rootDeviceId = rootDeviceId;
  }

  connect() {
    if (this.source) {
      return;
    }

    this.source = new EventSource(
      `/devices/${this.rootDeviceId}/reachable-devices`,
    );

    this.source.onmessage = (event) => {
      this.handleMessage(event.data);
    };

    this.source.onerror = () => {
      this.log("SSE connection error");
    };

    this.log("SSE connected");
  }

  disconnect() {
    this.source?.close();
    this.source = null;
    this.log("SSE disconnected");
  }

  private handleMessage(rawData: string) {
    let dto: SseEventDTO;

    try {
      dto = JSON.parse(rawData);
    } catch {
      this.log("Failed to parse SSE message");
      return;
    }

    switch (dto.type) {
      case "INITIAL_STATE":
        this.handleInitialState(dto);
        break;

      case "DEVICE_STATE_CHANGE":
        this.handleDeviceStateChange(dto);
        break;

      default:
        this.log(`Unknown SSE event type`);
    }
  }

  private handleInitialState(dto: InitialStateDTO) {
    networkStore.dispatch({
      type: "INITIAL_STATE_RECEIVED",
      payload: {
        rootDeviceId: this.rootDeviceId,
        deviceIds: dto.deviceIds,
      },
    });

    this.log(`Initial state received (${dto.deviceIds.length} devices)`);
  }

  private handleDeviceStateChange(dto: DeviceStateChangeDTO) {
    networkStore.dispatch({
      type: "DEVICE_STATE_CHANGED",
      payload: {
        deviceId: dto.deviceId,
      },
    });

    this.log(`Device ${dto.deviceId} state changed`);
  }

  private log(message: string) {
    eventLogStore.dispatch({
      type: "ADD_EVENT",
      payload: {
        id: crypto.randomUUID(),
        timestamp: Date.now(),
        type: "SSE_UPDATE",
        message,
      },
    });
  }
}
