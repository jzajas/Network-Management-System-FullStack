import { useEffect, useState } from "react";
import type { NetworkState } from "../models/NetworkState";
import { networkStore } from "../state/networkStore";

export function useNetworkState(): NetworkState {
  const [state, setState] = useState<NetworkState>(networkStore.getState());

  useEffect(() => {
    const unsubscribe = networkStore.subscribe(setState);
    return unsubscribe;
  }, []);

  return state;
}
