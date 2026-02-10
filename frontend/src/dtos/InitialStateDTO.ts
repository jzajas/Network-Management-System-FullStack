import type { BasicStateDTO } from "./BasicStateDTO";

export interface InitialStateDTO extends BasicStateDTO {
  type: "INITIAL_STATE";
  deviceIds: number[];
}
