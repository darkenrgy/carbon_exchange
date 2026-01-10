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
      <Route path="/" element={<Register />} />
      <Route path="/register" element={<Register />} />
      <Route path="/login" element={<Login />} />


      <Route
        path="/admin"
        element={
          <ProtectedRoute role="admin">
            <Admin />
          </ProtectedRoute>
        }
      />

      <Route
        path="/company"
        element={
          <ProtectedRoute role="company">
            <Company />
          </ProtectedRoute>
        }
      />

      <Route
        path="/farmer"
        element={
          <ProtectedRoute role="farmer">
            <Farmer />
          </ProtectedRoute>
        }
      />
    </Routes>
  );
}

export default App;
