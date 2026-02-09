import { useEffect, useState } from "react";
import { networkStore, type NetworkState } from "../state/networkStore";

export function useNetworkState(): NetworkState {
  const [state, setState] = useState<NetworkState>(networkStore.getState());

  useEffect(() => {
    const unsubscribe = networkStore.subscribe(setState);
    return unsubscribe;
  }, []);

  return state;
}
