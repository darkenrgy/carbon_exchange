import { useState } from "react";
import { useNavigate } from "react-router-dom";

const Register = () => {
  const [role, setRole] = useState("farmer");
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    // abhi dummy submit
    alert("Registration submitted. Admin verification pending.");
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-green-50">
      <div className="bg-white w-full max-w-xl rounded-2xl shadow-lg p-8">

        {/* Title */}
        <h2 className="text-2xl font-bold text-center mb-6 flex items-center justify-center gap-2">
          <span>👤</span> Create New Account
        </h2>

        {/* Role Toggle */}
        <div className="flex justify-center gap-4 mb-6">
          <button
            onClick={() => setRole("farmer")}
            className={`px-4 py-2 rounded-lg font-medium ${
              role === "farmer"
                ? "bg-green-600 text-white"
                : "bg-gray-200"
            }`}
          >
            🌱 Farmer
          </button>

          <button
            onClick={() => setRole("company")}
            className={`px-4 py-2 rounded-lg font-medium ${
              role === "company"
                ? "bg-green-600 text-white"
                : "bg-gray-200"
            }`}
          >
            🏭 Company
          </button>
        </div>

        <h3 className="text-green-700 font-semibold mb-4 capitalize">
          {role} Registration
        </h3>

        <form onSubmit={handleSubmit} className="space-y-4">

          <input className="input" placeholder="Full Name" required />
          <input className="input" placeholder="Mobile Number" required />
          <input className="input" placeholder="Aadhaar Number" />
          <input className="input" placeholder="PAN Number" />
          <input className="input" placeholder="Village / District / State" />

          {role === "farmer" && (
            <input className="input" placeholder="Land Area (in hectares)" />
          )}

          <div>
            <input type="file" className="w-full text-sm" />
            <p className="text-xs text-gray-500 mt-1">
              Upload land documents & soil report
            </p>
          </div>

          <input
            type="password"
            className="input"
            placeholder="Create Password"
            required
          />

          <button
            type="submit"
            className="w-full bg-green-600 text-white py-2 rounded-lg font-semibold hover:bg-green-700 transition"
          >
            Register as {role === "farmer" ? "Farmer" : "Company"}
          </button>
        </form>

        {/* Footer */}
        <p className="text-xs text-center text-gray-500 mt-4">
          All accounts are verified by admin before activation
        </p>

        {/* Login Link */}
        <p className="text-center text-sm mt-4">
          Already registered?{" "}
          <span
            onClick={() => navigate("/login")}
            className="text-green-600 font-semibold cursor-pointer hover:underline"
          >
            Login
          </span>
        </p>
      </div>
    </div>
  );
};

export default Register;
