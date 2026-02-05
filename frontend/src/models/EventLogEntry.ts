export type EventType =
  | "SSE_UPDATE"
  | "PATCH_SENT"
  | "PATCH_SUCCESS"
  | "PATCH_ERROR"
  | "CONNECTION_STATUS";

export interface EventLogEntry {
  id: string;
  timestamp: number;
  type: EventType;
  message: string;
}
