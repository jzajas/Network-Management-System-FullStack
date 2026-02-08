import type { BasicStateDTO } from "./BasicStateDTO";

export interface DeviceStateChangeDTO extends BasicStateDTO {
  type: "DEVICE_STATE_CHANGE";
  deviceId: number;
}
