import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.tsx'
import "./styles/globals.css";
import { NetworkProvider } from './state/NetworkContext.tsx';
import { EventLogProvider } from './state/EventLogContext.tsx';

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <NetworkProvider>
      <EventLogProvider>
        <App />
      </EventLogProvider>
    </NetworkProvider>
  </StrictMode>,
)
