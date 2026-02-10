import { useNetworkState } from "../hooks/useNetworkState";
import type { D3Data, D3Node, D3Link } from "./Types";

export function useGraphData(rootDeviceId: number | null): D3Data {
  const network = useNetworkState();

  const nodes: D3Node[] = network.nodes.map((node) => ({
    id: node.id,
    group:
      node.id === rootDeviceId
        ? "root"
        : node.visible
          ? "reachable"
          : "disabled",
  }));

  const links: D3Link[] = network.edges.map((edge) => ({
    source: edge.from,
    target: edge.to,
  }));

  return { nodes, links };
}
