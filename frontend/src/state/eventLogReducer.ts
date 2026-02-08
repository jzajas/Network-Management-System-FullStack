import type { EventLogEntry } from "../models/EventLogEntry";
import type { EventLogAction } from "./EventLogAction";

export interface EventLogState {
  events: EventLogEntry[];
}

export const initialEventLogState: EventLogState = {
  events: [],
};

const MAX_EVENTS = 200;

export function eventLogReducer(
  state: EventLogState,
  action: EventLogAction,
): EventLogState {
  switch (action.type) {
    case "ADD_EVENT": {
      const nextEvents = [...state.events, action.payload];

      return {
        events:
          nextEvents.length > MAX_EVENTS
            ? nextEvents.slice(-MAX_EVENTS)
            : nextEvents,
      };
    }

    case "CLEAR":
      return {
        events: [],
      };

    default:
      return state;
  }
}
