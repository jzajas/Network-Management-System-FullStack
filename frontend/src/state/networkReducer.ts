import type { NetworkSnapshot } from "../models/NetworkSnapshot"; 
import type { NetworkAction } from "./networkActions";

export interface NetworkState {
  snapshot: NetworkSnapshot | null;
}

export const initialNetworkState: NetworkState = {
  snapshot: null,
};

export function networkReducer(
  state: NetworkState,
  action: NetworkAction
): NetworkState {
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
              : node
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
              : node
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
