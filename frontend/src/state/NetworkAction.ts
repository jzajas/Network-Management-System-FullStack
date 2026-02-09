export type NetworkAction =
  | {
      type: "INITIAL_STATE_RECEIVED";
      payload: {
        rootDeviceId: number;
        deviceIds: number[];
      };
    }
  | {
      type: "DEVICE_STATE_CHANGED";
      payload: {
        deviceId: number;
        visible: boolean;
      };
    };
