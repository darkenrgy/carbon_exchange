import { NavLink, useNavigate } from "react-router-dom";

const Sidebar = () => {
  const navigate = useNavigate();
  const role = localStorage.getItem("role"); // Admin | Farmer | Company

  const menu = {
    Admin: [
      { name: "Dashboard", path: "/admin" },
      { name: "Farmers", path: "/admin/users" },
      { name: "Companies", path: "/admin/companies" },
      { name: "Credits", path: "/admin/credits" },
      { name: "Verification", path: "/admin/verify" },
    ],
    Farmer: [
      { name: "Overview", path: "/farmer" },
      { name: "Crop", path: "/farmer/crop" },
      { name: "Verification", path: "/farmer/verify" },
      { name: "Wallet", path: "/farmer/wallet" },
      { name: "Profile", path: "/farmer/profile" },
    ],
    Company: [
      { name: "Market", path: "/company" },
      { name: "Buy Credits", path: "/company/buy" },
      { name: "Compliance", path: "/company/compliance" },
      { name: "Wallet", path: "/company/wallet" },
      { name: "Profile", path: "/company/profile" },
    ],
  };

  if (!role || !menu[role]) return null;

  const logout = () => {
    localStorage.clear();
    navigate("/login");
  };

  return (
    <aside className="w-64 bg-white shadow-md rounded-xl p-4 h-fit">
      <h2 className="text-xl font-bold text-green-600 mb-6">
        {role} Panel
      </h2>

      <nav className="space-y-2">
        {menu[role].map((item, index) => (
          <NavLink
            key={index}
            to={item.path}
            className={({ isActive }) =>
              `block px-4 py-2 rounded-lg ${
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
    </aside>
  );
};

export default Sidebar;
