import { useEffect, useState } from "react";
import type { EventLogEntry } from "../models/EventLogEntry";
import { eventLogStore } from "../state/eventLogStore";

export function useEventLog(): EventLogEntry[] {
  const [entries, setEntries] = useState<EventLogEntry[]>(
    eventLogStore.getState(),
  );

  useEffect(() => {
    const unsubscribe = eventLogStore.subscribe(setEntries);
    return unsubscribe;
  }, []);

  return entries;
}
