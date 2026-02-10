import type { NetworkState } from "../models/NetworkState";
import type { NetworkAction } from "./NetworkAction";

type Listener = (state: NetworkState) => void;

class NetworkStore {
  private state: NetworkState = {
    rootDeviceId: null,
    nodes: [],
    edges: [],
  };

  private listeners: Listener[] = [];

  getState(): NetworkState {
    return this.state;
  }

  dispatch(action: NetworkAction) {
    this.state = this.reduce(this.state, action);
    this.notify();
  }

  subscribe(listener: Listener) {
    this.listeners.push(listener);

    listener(this.state);

    return () => {
      this.listeners = this.listeners.filter((l) => l !== listener);
    };
  }

  private reduce(state: NetworkState, action: NetworkAction): NetworkState {
    switch (action.type) {
      case "INITIAL_STATE_RECEIVED":
        return {
          rootDeviceId: action.payload.rootDeviceId,
          nodes: action.payload.deviceIds.map((id) => ({
            id,
            visible: true,
          })),
          edges: action.payload.edges,
        };

      case "DEVICE_STATE_CHANGED":
        return {
          ...state,
          nodes: state.nodes.map((node) =>
            node.id === action.payload.deviceId
              ? { ...node, visible: !node.visible }
              : node,
          ),
        };

      default:
        return state;
    }
  }

  private notify() {
    this.listeners.forEach((listener) => listener(this.state));
  }
}

export const networkStore = new NetworkStore();
