import type { NetworkNode } from "./NetworkNode";
import type { NetworkEdge } from "./NetworkEdge";

export interface NetworkState {
  rootDeviceId: number | null;
  nodes: NetworkNode[];
  edges: NetworkEdge[];
}
