import type { DeviceStateChangeDTO } from "./DeviceStateChangeDTO";
import type { InitialStateDTO } from "./InitialStateDTO";

export type SseEventDTO =
  | InitialStateDTO
  | DeviceStateChangeDTO;