export type DeviceStatus = "ON" | "OFF";

export interface Device {
  id: number;
  status: DeviceStatus;
}