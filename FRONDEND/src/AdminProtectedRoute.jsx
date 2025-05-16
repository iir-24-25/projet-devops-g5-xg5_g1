import { Navigate } from "react-router-dom";

export default function AdminProtectedRoute({ children }) {
    const isAuthenticated = localStorage.getItem("username");
    const role = localStorage.getItem("role");

    if (!isAuthenticated || role !== "ADMINISTRATEUR") {
        return <Navigate to="/" replace />;
    }

    return children;
}
