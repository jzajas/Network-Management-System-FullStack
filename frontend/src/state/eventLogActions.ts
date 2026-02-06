import type { EventLogEntry } from "../models/EventLogEntry";

export type EventLogAction =
  | { type: "ADD_EVENT"; payload: EventLogEntry }
  | { type: "CLEAR" };
