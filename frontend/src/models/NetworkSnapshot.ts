import type { NetworkNode } from "./NetworkNode";
import type { NetworkEdge } from "./NetworkEdge";

export interface NetworkSnapshot {
  rootDeviceId: number;
  nodes: NetworkNode[];
  edges: NetworkEdge[];
}
