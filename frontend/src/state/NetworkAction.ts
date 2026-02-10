import type { NetworkEdge } from "../models/NetworkEdge";

export type NetworkAction =
  | {
      type: "INITIAL_STATE_RECEIVED";
      payload: {
        rootDeviceId: number;
        deviceIds: number[];
        edges: NetworkEdge[];
      };
    }
  | {
      type: "DEVICE_STATE_CHANGED";
      payload: {
        deviceId: number;
        visible: boolean;
      };
    };
