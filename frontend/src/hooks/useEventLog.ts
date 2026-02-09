import { useEffect, useState } from "react";
import { eventLogStore } from "../state/eventLogStore";
import type { EventLogEntry } from "../models/EventLogEntry";

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
