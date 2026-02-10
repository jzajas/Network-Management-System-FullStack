export type GraphNodeGroup = "root" | "reachable" | "disabled";

export interface D3Node {
  id: number;
  group: GraphNodeGroup;
  x?: number;
  y?: number;
}

export interface D3Link {
  source: number | D3Node;
  target: number | D3Node;
}

export interface D3Data {
  nodes: D3Node[];
  links: D3Link[];
}
