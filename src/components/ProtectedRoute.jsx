import { Navigate } from "react-router-dom";

const ProtectedRoute = ({ children, allowedRole }) => {
  const isLoggedIn = localStorage.getItem("isLoggedIn");
  const role = localStorage.getItem("role");

  if (!isLoggedIn) {
    return <Navigate to="/" replace />;
  }

  if (allowedRole && role !== allowedRole) {
    return <Navigate to="/" replace />;
  }

  return children;
};

export default ProtectedRoute;
