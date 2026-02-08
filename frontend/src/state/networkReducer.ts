import type { NetworkAction } from "./NetworkAction";
import type { NetworkState } from "./networkStore";

export interface NetworkReducerState {
  snapshot: NetworkState | null;
}

export const initialNetworkState: NetworkReducerState = {
  snapshot: null,
};

export function networkReducer(
  state: NetworkReducerState,
  action: NetworkAction,
): NetworkReducerState {
  switch (action.type) {
    case "SET_SNAPSHOT":
      return {
        snapshot: action.payload,
      };

    case "HIDE_NODE":
      if (!state.snapshot) return state;

      return {
        snapshot: {
          ...state.snapshot,
          nodes: state.snapshot.nodes.map((node) =>
            node.id === action.payload.nodeId
              ? { ...node, visible: false }
              : node,
          ),
        },
      };

    case "SHOW_NODE":
      if (!state.snapshot) return state;

      return {
        snapshot: {
          ...state.snapshot,
          nodes: state.snapshot.nodes.map((node) =>
            node.id === action.payload.nodeId
              ? { ...node, visible: true }
              : node,
          ),
        },
      };

    case "CLEAR":
      return {
        snapshot: null,
      };

    default:
      return state;
  }
}
