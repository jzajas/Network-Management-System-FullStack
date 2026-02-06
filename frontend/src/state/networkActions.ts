import type { NetworkSnapshot } from "../models/NetworkSnapshot"; 

export type NetworkAction =
  | { type: "SET_SNAPSHOT"; payload: NetworkSnapshot }
  | { type: "HIDE_NODE"; payload: { nodeId: number } }
  | { type: "SHOW_NODE"; payload: { nodeId: number } }
  | { type: "CLEAR" };