import AppLayout from "./layout/AppLayout";
import { BrowserRouter, Routes, Route } from "react-router-dom";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/devices/:deviceId" element={<AppLayout />} />
      </Routes>
    </BrowserRouter>
  );
}
