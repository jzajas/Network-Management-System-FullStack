import { useNetworkState } from "../hooks/useNetworkState";
import type { GraphNode, GraphLink } from "./Types";

export function useGraphData(rootDeviceId: number | null) {
  const network = useNetworkState();

  const nodes: GraphNode[] = network.nodes.map((node) => ({
    id: node.id,
    group:
      node.id === rootDeviceId
        ? "root"
        : node.visible
          ? "reachable"
          : "disabled",
  }));

  const links: GraphLink[] = network.edges.map((edge) => ({
    source: edge.from,
    target: edge.to,
  }));

  return { nodes, links };
}
