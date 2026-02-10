import { Navigate, Route, Routes } from "react-router-dom";
import AppLayout from "./layout/AppLayout";

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/devices/0" replace />} />
      <Route path="/devices/:id" element={<AppLayout />} />
    </Routes>
  );
}
