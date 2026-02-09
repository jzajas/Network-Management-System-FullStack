import { useMemo } from "react";
import { useNetworkState } from "../../hooks/useNetworkState";
import type { D3Data } from "../Types";

export function useD3NetworkData(rootDeviceId: number): D3Data {
  const network = useNetworkState();

  return useMemo(() => {
    return {
      nodes: network.nodes.map((node) => ({
        id: node.id,
        group:
          node.id === rootDeviceId
            ? "root"
            : node.visible
              ? "reachable"
              : "disabled",
      })),
      links: network.edges.map((edge) => ({
        source: edge.from,
        target: edge.to,
      })),
    };
  }, [network.nodes, network.edges, rootDeviceId]);
}
