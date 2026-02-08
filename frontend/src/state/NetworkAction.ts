import type { NetworkState } from "../models/NetworkState";

export type NetworkAction =
  | { type: "SET_SNAPSHOT"; payload: NetworkState }
  | { type: "HIDE_NODE"; payload: { nodeId: number } }
  | { type: "SHOW_NODE"; payload: { nodeId: number } }
  | { type: "CLEAR" };
