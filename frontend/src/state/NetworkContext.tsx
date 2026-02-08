import { createContext, useContext, useReducer } from "react";
import type { ReactNode, Dispatch } from "react";
import type { NetworkAction } from "./NetworkAction";
import {
  type NetworkState,
  networkReducer,
  initialNetworkState,
} from "./networkReducer";

const erorMessage = "useNetwork must be used within a NetworkProvider";

type NetworkContextValue = {
  state: NetworkState;
  dispatch: Dispatch<NetworkAction>;
};

const NetworkContext = createContext<NetworkContextValue | null>(null);

type NetworkProviderProps = {
  children: ReactNode;
};

export function NetworkProvider({ children }: NetworkProviderProps) {
  const [state, dispatch] = useReducer(networkReducer, initialNetworkState);

  return (
    <NetworkContext.Provider value={{ state, dispatch }}>
      {children}
    </NetworkContext.Provider>
  );
}

// eslint-disable-next-line react-refresh/only-export-components
export function useNetwork() {
  const context = useContext(NetworkContext);
  if (context === null) {
    throw new Error(erorMessage);
  }
  return context;
}
