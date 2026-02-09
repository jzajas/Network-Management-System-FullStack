export type GraphNodeGroup = "root" | "reachable" | "disabled";

export type GraphNode = {
  id: number;
  group: "root" | "reachable" | "disabled";
};

export type GraphLink = {
  source: number;
  target: number;
};

export interface D3Node {
  id: number;
  group: "root" | "reachable" | "disabled";
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
