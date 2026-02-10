import type { D3Node, D3Link } from "./Types";

export const RADIUS = 10;

export function drawNetwork(
  context: CanvasRenderingContext2D,
  width: number,
  height: number,
  nodes: D3Node[],
  links: D3Link[],
) {
  context.clearRect(0, 0, width, height);

  links.forEach((link) => {
    const source = link.source as D3Node;
    const target = link.target as D3Node;

    if (!source || !target) {
      return;
    }

    const bothEnabled =
      source.group !== "disabled" && target.group !== "disabled";

    context.strokeStyle = bothEnabled ? "#f2ff00" : "#fd0000";
    context.globalAlpha = 1.0;
    context.lineWidth = 2;
    
    

    context.beginPath();
    context.moveTo(source.x!, source.y!);
    context.lineTo(target.x!, target.y!);
    context.stroke();
  });

  context.globalAlpha = 1.0;
  context.strokeStyle = "#ffffff";

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
