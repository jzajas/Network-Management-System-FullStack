import * as d3 from "d3";
import { useEffect, useRef, useState } from "react";
import { drawNetwork, RADIUS } from "./drawNetwork";
import type { D3Data, D3Node, D3Link } from "./Types";

export function NetworkDiagram({ data }: { data: D3Data }) {
  const containerRef = useRef<HTMLDivElement>(null);
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const [dimensions, setDimensions] = useState({ width: 0, height: 0 });

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

  return (
    <div ref={containerRef} className="w-full h-full">
      <canvas
        ref={canvasRef}
        width={dimensions.width}
        height={dimensions.height}
        style={{ width: "100%", height: "100%" }}
      />
    </div>
  );
}
