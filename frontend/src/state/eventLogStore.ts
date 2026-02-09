import type { EventLogEntry } from "../models/EventLogEntry";
import type { EventLogAction } from "./EventLogAction";

type Listener = (entries: EventLogEntry[]) => void;

class EventLogStore {
  private entries: EventLogEntry[] = [];
  private listeners: Listener[] = [];

  getState(): EventLogEntry[] {
    return this.entries;
  }

  dispatch(action: EventLogAction) {
    switch (action.type) {
      case "ADD_EVENT":
        this.entries = [...this.entries, action.payload];
        break;

      case "CLEAR":
        this.entries = [];
        break;
    }

    this.notify();
  }

  subscribe(listener: Listener) {
    this.listeners.push(listener);

    listener(this.entries);

    return () => {
      this.listeners = this.listeners.filter((l) => l !== listener);
    };
  }

  private notify() {
    this.listeners.forEach((listener) => listener(this.entries));
  }
}

export const eventLogStore = new EventLogStore();
