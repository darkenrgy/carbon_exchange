import { NavLink, useNavigate } from "react-router-dom";

const Sidebar = () => {
  const navigate = useNavigate();

  // JWT-based auth
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role"); // ADMIN | FARMER | COMPANY

  // If no token → do not render sidebar
  if (!token || !role) return null;

  const menu = {
    ADMIN: [
      { name: "Dashboard", path: "/admin" },
      { name: "Farmers", path: "/admin/farmers" },
      { name: "Companies", path: "/admin/companies" },
      { name: "Credits", path: "/admin/credits" },
      { name: "Verification", path: "/admin/verify" },
    ],
    FARMER: [
      { name: "Overview", path: "/farmer" },
      { name: "Crop Data", path: "/farmer/crop" },
      { name: "Verification", path: "/farmer/verify" },
      { name: "Wallet", path: "/farmer/wallet" },
      { name: "Profile", path: "/farmer/profile" },
    ],
    COMPANY: [
      { name: "Market", path: "/company" },
      { name: "Buy Credits", path: "/company/buy" },
      { name: "Compliance", path: "/company/compliance" },
      { name: "Wallet", path: "/company/wallet" },
      { name: "Profile", path: "/company/profile" },
    ],
  };

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    navigate("/login");
  };

  return (
    <aside className="w-64 bg-white shadow-md rounded-xl p-4 h-screen">
      <h2 className="text-xl font-bold text-green-600 mb-6">
        {role} PANEL
      </h2>

      <nav className="space-y-2">
        {menu[role]?.map((item, index) => (
          <NavLink
            key={index}
            to={item.path}
            className={({ isActive }) =>
              `block px-4 py-2 rounded-lg transition ${
                isActive
                  ? "bg-green-600 text-white"
                  : "text-gray-700 hover:bg-green-100"
              }`
            }
          >
            {item.name}
          </NavLink>
        ))}
      </nav>

      {/* Logout */}
      <button
        onClick={logout}
        className="mt-6 w-full bg-red-500 text-white py-2 rounded-lg hover:bg-red-600 transition"
      >
        Logout
      </button>
    </aside>
  );
};

export default Sidebar;

