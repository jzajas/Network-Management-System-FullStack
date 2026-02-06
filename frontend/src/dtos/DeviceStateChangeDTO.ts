import type { BasicStateDTO } from "./SseEventDTOs";

export interface DeviceStateChangeDTO extends BasicStateDTO {
  type: "DEVICE_STATE_CHANGE";
  deviceId: number;
}