import { useEffect } from "react";
import { useParams } from "react-router-dom";
import { useEventLog } from "../hooks/useEventLog";
import { NetworkSseClient } from "../sse/NetworkSseClient";
import { eventLogStore } from "../state/eventLogStore";
import { networkStore } from "../state/networkStore";

export default function AppLayout() {
  const { id } = useParams<{ id: string }>();

  const rootDeviceId = Number(id);
  const events = useEventLog();

  useEffect(() => {
    if (Number.isNaN(rootDeviceId)) return;

    const sseClient = new NetworkSseClient();

    sseClient.connect(
      rootDeviceId,
      networkStore.dispatch.bind(networkStore),
      eventLogStore.dispatch.bind(eventLogStore),
    );

    return () => {
      sseClient.disconnect();
    };
  }, [rootDeviceId]);

  return (
    <div className="h-screen w-screen bg-slate-950 text-slate-200 flex">
      <main className="flex-1 relative flex items-center justify-center border-r border-slate-800">
        <div className="absolute top-4 left-4 text-sm text-slate-400">
          Network Topology – Root device {rootDeviceId}
        </div>
        <div className="flex items-center justify-center text-slate-600">
          <span>Graph will render here</span>
        </div>
      </main>

      <aside className="w-[360px] flex flex-col bg-slate-900">
        <header className="h-14 px-4 flex items-center border-b border-slate-800">
          <h2 className="text-sm font-semibold tracking-wide">Event Feed</h2>
        </header>
        <section className="flex-1 overflow-y-auto p-4 space-y-3 text-sm font-mono">
          {events.length === 0 ? (
            <div className="text-slate-500">Waiting for events…</div>
          ) : (
            events.map((event) => (
              <pre
                key={event.id}
                className="bg-slate-800 p-2 rounded text-slate-200"
              >
                {event.message}
              </pre>
            ))
          )}
        </section>

        <footer className="h-10 px-4 flex items-center border-t border-slate-800 text-xs text-slate-500">
          SSE: {events.length > 0 ? "connected" : "disconnected"}
        </footer>
      </aside>
    </div>
  );
}
