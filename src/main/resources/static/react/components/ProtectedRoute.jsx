import { Navigate } from "react-router-dom";

const ProtectedRoute = ({ children, allowedRoles }) => {
    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");

    // 1. Not logged in → go to login
    if (!token) {
        return <Navigate to="/login" replace />;
    }

    // 2. Role not allowed
    if (allowedRoles && !allowedRoles.includes(role)) {
        return <Navigate to="/login" replace />;
    }

    return children;
};

export default ProtectedRoute;
