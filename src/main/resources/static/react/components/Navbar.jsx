import { useNavigate } from "react-router-dom";

const Navbar = () => {
    const navigate = useNavigate();

    const role = localStorage.getItem("role");
    const token = localStorage.getItem("token");

    const logout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("role");
        navigate("/login");
    };

    // Optional safety check
    if (!token) {
        navigate("/login");
        return null;
    }

    return (
        <div className="h-20 bg-green-700 text-white flex items-center justify-between px-6 shadow">
            <h1 className="font-bold text-lg">
                Carbon Exchange Platform
            </h1>

            <div className="flex items-center gap-4">
        <span className="capitalize font-medium">
          {role?.toLowerCase()} Dashboard
        </span>

                <button
                    onClick={logout}
                    className="bg-red-500 hover:bg-red-600 px-4 py-1 rounded transition"
                >
                    Logout
                </button>
            </div>
        </div>
    );
};

export default Navbar;
