import * as d3 from "d3";
import { useEffect, useRef, useState } from "react";
import { drawNetwork, RADIUS } from "./drawNetwork";
import type { D3Data, D3Node, D3Link } from "./Types";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

export function NetworkDiagram({ data }: { data: D3Data }) {
  const containerRef = useRef<HTMLDivElement>(null);
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const [dimensions, setDimensions] = useState({ width: 0, height: 0 });

  const nodesRef = useRef<D3Node[]>([]);


  useEffect(() => {
    if (!containerRef.current) return;

    const observer = new ResizeObserver(() => {
      setDimensions({
        width: containerRef.current!.clientWidth,
        height: containerRef.current!.clientHeight,
      });
    });

    observer.observe(containerRef.current);
    return () => observer.disconnect();
  }, []);

  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;
    const context = canvas.getContext("2d");
    if (!context) return;
    if (dimensions.width === 0 || dimensions.height === 0) return;

    const nodes: D3Node[] = data.nodes.map((d) => ({ ...d }));
    const links: D3Link[] = data.links.map((d) => ({ ...d }));
    
    nodesRef.current = nodes;

    const simulation = d3
      .forceSimulation<D3Node>(nodes)
      .force(
        "link",
        d3.forceLink<D3Node, D3Link>(links).id((d: D3Node) => d.id),
      )
      .force("collide", d3.forceCollide().radius(RADIUS * 2))
      .force("charge", d3.forceManyBody().strength(-50))
      .force(
        "center",
        d3.forceCenter(dimensions.width / 2, dimensions.height / 2),
      )
      .on("tick", () => {
        nodes.forEach((node) => {
          node.x = Math.max(
            RADIUS,
            Math.min(dimensions.width - RADIUS, node.x!),
          );
          node.y = Math.max(
            RADIUS,
            Math.min(dimensions.height - RADIUS, node.y!),
          );
        });

        drawNetwork(context, dimensions.width, dimensions.height, nodes, links);
      });

    return () => {
      simulation.stop();
      return;
    };
  }, [data, dimensions.width, dimensions.height]);

  const handleCanvasClick = (event: React.MouseEvent<HTMLCanvasElement>) => {
    const canvas = canvasRef.current;
    if (!canvas) return;

    const rect = canvas.getBoundingClientRect();
    const scaleX = canvas.width / rect.width;
    const scaleY = canvas.height / rect.height;
    
    const mouseX = (event.clientX - rect.left) * scaleX;
    const mouseY = (event.clientY - rect.top) * scaleY;

    const clickedNode = nodesRef.current.find((node) => {
      if (node.x === undefined || node.y === undefined) return false;
      const distance = Math.sqrt(
        Math.pow(mouseX - node.x, 2) + Math.pow(mouseY - node.y, 2)
      );
      return distance <= RADIUS;
    });

    if (clickedNode) {
      handleNodeClick(clickedNode.id, clickedNode.group);
    }
  };

  const handleNodeClick = async (nodeId: number, currentGroup: string) => {
    console.log(`Node clicked: ID=${nodeId}, Group=${currentGroup}`);
    if (currentGroup === "root") {
      console.log("Cannot toggle root node");
      return;
    }

    try {
      let newState : boolean;
      switch (currentGroup) {
        case "reachable":
          newState = false
          break;
        case "disabled":
          newState = true
          break;
        default:
          console.warn(`Unknown group "${currentGroup}" for node ${nodeId}`);
          return;
      }
      
      await fetch(`${API_BASE_URL}/devices/${nodeId}`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ active: newState }),
      });
      
    } catch (error) {
      console.error("Failed to toggle device:", error);
    }
  };

  return (
    <div ref={containerRef} className="w-full h-full">
      <canvas
        ref={canvasRef}
        width={dimensions.width}
        height={dimensions.height}
        style={{ width: "100%", height: "100%", cursor: "pointer" }}
        onClick={handleCanvasClick}
      />
    </div>
  );
}
