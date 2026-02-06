export type EventTypes =
  | "INITIAL_STATE"
  | "DEVICE_STATE_CHANGE";

export interface BasicStateDTO {
  type: EventTypes;
}