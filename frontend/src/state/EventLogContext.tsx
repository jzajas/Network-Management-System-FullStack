import { createContext, useContext, useReducer } from "react";
import type { ReactNode, Dispatch } from "react";
import { eventLogReducer, initialEventLogState, type EventLogState } from "./eventLogReducer";
import type { EventLogAction } from "./eventLogActions";

type EventLogContextValue = {
  state: EventLogState;
  dispatch: Dispatch<EventLogAction>;
};

const EventLogContext =
  createContext<EventLogContextValue | null>(null);

type EventLogProviderProps = {
  children: ReactNode;
};

export function EventLogProvider({
  children,
}: EventLogProviderProps) {
  const [state, dispatch] = useReducer(
    eventLogReducer,
    initialEventLogState
  );

  return (
    <EventLogContext.Provider value={{ state, dispatch }}>
      {children}
    </EventLogContext.Provider>
  );
}

// eslint-disable-next-line react-refresh/only-export-components
export function useEventLog() {
  const context = useContext(EventLogContext);
  if (context === null) {
    throw new Error(
      "useEventLog must be used within an EventLogProvider"
    );
  }
  return context;
}
