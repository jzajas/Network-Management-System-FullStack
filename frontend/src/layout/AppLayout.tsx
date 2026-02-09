import { useEffect } from "react";
import { useNetworkState } from "../hooks/useNetworkState";
import { useEventLog } from "../hooks/useEventLog";
import { NetworkSseClient } from "../sse/NetworkSseClient";

export default function AppLayout() {
  const network = useNetworkState();
  const events = useEventLog();

  useEffect(() => {
    const rootDeviceId = 1;

    const sseClient = new NetworkSseClient(rootDeviceId);
    sseClient.connect();

    return () => {
      sseClient.disconnect();
    };
  }, []);

  return (
    <div style={{ padding: 16 }}>
      <h1>Network Debug View</h1>

      <section style={{ marginBottom: 24 }}>
        <h2>Network State</h2>

        <div>
          <strong>Root device:</strong> {network.rootDeviceId ?? "not set"}
        </div>

        <ul>
          {network.nodes.map((node) => (
            <li key={node.id}>
              Device {node.id} — {node.visible ? "VISIBLE" : "HIDDEN"}
            </li>
          ))}
        </ul>
      </section>

      <section>
        <h2>Event Log</h2>

        <ul>
          {events.map((event) => (
            <li key={event.id}>
              [{new Date(event.timestamp).toLocaleTimeString()}] {event.message}
            </li>
          ))}
        </ul>
      </section>
    </div>
  );
}
