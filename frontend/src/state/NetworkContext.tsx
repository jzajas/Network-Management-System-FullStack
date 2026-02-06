import { createContext, useContext, useReducer } from "react";
import type { ReactNode, Dispatch } from "react";
import { networkReducer, initialNetworkState, type NetworkState } from "./networkReducer";
import type { NetworkAction } from "./networkActions";

type NetworkContextValue = {
  state: NetworkState;
  dispatch: Dispatch<NetworkAction>;
};

const NetworkContext = createContext<NetworkContextValue | null>(null);

type NetworkProviderProps = {
  children: ReactNode;
};

export function NetworkProvider({ children }: NetworkProviderProps) {
  const [state, dispatch] = useReducer(
    networkReducer,
    initialNetworkState
  );

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
    throw new Error(
      "useNetwork must be used within a NetworkProvider"
    );
  }
  return context;
}
