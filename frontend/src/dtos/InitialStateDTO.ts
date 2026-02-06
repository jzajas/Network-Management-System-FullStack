import type { BasicStateDTO } from "./SseEventDTOs";

export interface InitialStateDTO extends BasicStateDTO {
  type: "INITIAL_STATE";
  deviceIds: number[];
}