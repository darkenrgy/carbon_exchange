import { useState } from "react";
import { useNavigate } from "react-router-dom";

const Login = () => {
  const navigate = useNavigate();
  const [role, setRole] = useState("Admin");

  const handleLogin = (e) => {
  e.preventDefault();
  
  localStorage.setItem("isLoggedIn", "true");
  localStorage.setItem("role", role);
  
  if (role === "Admin") {
    navigate("/admin");
  } else if (role === "Farmer") {
    navigate("/farmer");
  } else if (role === "Company") {
    navigate("/company");
  }
};


  return (
    <div className="min-h-screen flex items-center justify-center bg-green-100">
      <div className="bg-white rounded-2xl shadow-lg w-full max-w-md p-8">

        {/* Title */}
        <h2 className="text-2xl font-bold text-center mb-6">
          Carbon Exchange Platform Login
        </h2>

        <form onSubmit={handleLogin} className="space-y-4">

          {/* Login As */}
          <div>
            <label className="text-sm font-medium">Login As</label>
            <select
              value={role}
              onChange={(e) => setRole(e.target.value)}
              className="w-full mt-1 px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
            >
              <option>Admin</option>
              <option>Farmer</option>
              <option>Company</option>
            </select>
          </div>

          {/* Email */}
          <div>
            <label className="text-sm font-medium">Email</label>
            <input
              type="email"
              placeholder="example@email.com"
              className="w-full mt-1 px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
              required
            />
          </div>

          {/* Password */}
          <div>
            <label className="text-sm font-medium">Password</label>
            <input
              type="password"
              placeholder="••••••••"
              className="w-full mt-1 px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
              required
            />
          </div>

          {/* Login Button */}
          <button
            type="submit"
            className="w-full bg-green-600 text-white py-2 rounded-lg font-semibold hover:bg-green-700 transition"
          >
            Login
          </button>
        </form>

        {/* Footer */}
        <p className="text-xs text-center text-gray-500 mt-6">
          © 2026 Carbon Credit Trading Platform
        </p>
      </div>
    </div>
  );
};

export default Login;
