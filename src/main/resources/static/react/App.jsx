import { Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Admin from "./pages/Admin";
import Company from "./pages/Company";
import Farmer from "./pages/Farmer";
import ProtectedRoute from "./components/ProtectedRoute";

function App() {
  return (
    <Routes>
      {/* Default route */}
      <Route path="/" element={<Login />} />

      {/* Auth routes */}
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />

      {/* Admin Protected */}
      <Route
        path="/admin"
        element={
          <ProtectedRoute allowedRoles={["ADMIN"]}>
            <Admin />
          </ProtectedRoute>
        }
      />

      {/* Company Protected */}
      <Route
        path="/company"
        element={
          <ProtectedRoute allowedRoles={["COMPANY"]}>
            <Company />
          </ProtectedRoute>
        }
      />

      {/* Farmer Protected */}
      <Route
        path="/farmer"
        element={
          <ProtectedRoute allowedRoles={["FARMER"]}>
            <Farmer />
          </ProtectedRoute>
        }
      />
    </Routes>
  );
}

export default App;

