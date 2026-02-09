import type { D3Node, D3Link } from "./Types";

export const RADIUS = 6;

export function drawNetwork(
  context: CanvasRenderingContext2D,
  width: number,
  height: number,
  nodes: D3Node[],
  links: D3Link[],
) {
  context.clearRect(0, 0, width, height);

  console.log("Links count:", links.length);
  console.log("First link:", links[0]);

  context.strokeStyle = "#f2ff00";
  context.lineWidth = 1;
  links.forEach((link) => {
    const source = link.source as D3Node;
    const target = link.target as D3Node;

    console.log("Link source:", source, "target:", target);

    if (!source || !target) {
      console.log("Missing source or target!");
      return;
    }

    context.beginPath();
    context.moveTo(source.x!, source.y!);
    context.lineTo(target.x!, target.y!);
    context.stroke();
  });

  context.strokeStyle = "#e65050";

  nodes.forEach((node) => {
    context.beginPath();
    context.arc(node.x!, node.y!, RADIUS, 0, 2 * Math.PI);

    switch (node.group) {
      case "root":
        context.fillStyle = "#22c55e";
        break;
      case "disabled":
        context.fillStyle = "#ef4444";
        break;
      default:
        context.fillStyle = "#38bdf8";
    }

    context.fill();
    context.lineWidth = 2;
    context.stroke();
  });
}
