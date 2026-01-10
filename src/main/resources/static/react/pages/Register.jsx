import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const Register = () => {
    const navigate = useNavigate();

    const [role, setRole] = useState("farmer");
    const [formData, setFormData] = useState({
        name: "",
        email: "",
        password: ""
    });

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            await axios.post("http://localhost:8080/api/auth/register", {
                name: formData.name,
                email: formData.email,
                password: formData.password,
                role: role.toUpperCase()
            });

            alert("Registration successful! Await admin verification.");
            navigate("/login");

        } catch (error) {
            alert(
                error.response?.data?.message ||
                "Registration failed. Try again."
            );
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-green-50">
            <div className="bg-white w-full max-w-xl rounded-2xl shadow-lg p-8">

                <h2 className="text-2xl font-bold text-center mb-6">
                    👤 Create New Account
                </h2>

                {/* Role Toggle */}
                <div className="flex justify-center gap-4 mb-6">
                    <button
                        type="button"
                        onClick={() => setRole("farmer")}
                        className={`px-4 py-2 rounded-lg font-medium ${
                            role === "farmer" ? "bg-green-600 text-white" : "bg-gray-200"
                        }`}
                    >
                         Farmer
                    </button>

                    <button
                        type="button"
                        onClick={() => setRole("company")}
                        className={`px-4 py-2 rounded-lg font-medium ${
                            role === "company" ? "bg-green-600 text-white" : "bg-gray-200"
                        }`}
                    >
                         Company
                    </button>
                </div>

                <h3 className="text-green-700 font-semibold mb-4 capitalize">
                    {role} Registration
                </h3>

                <form onSubmit={handleSubmit} className="space-y-4">

                    {/* Backend required fields */}
                    <input
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        className="input"
                        placeholder="Full Name"
                        required
                    />

                    <input
                        name="email"
                        type="email"
                        value={formData.email}
                        onChange={handleChange}
                        className="input"
                        placeholder="Email Address"
                        required
                    />

                    <input
                        name="password"
                        type="password"
                        value={formData.password}
                        onChange={handleChange}
                        className="input"
                        placeholder="Create Password"
                        required
                    />

                    {/* Extra fields (KYC – future use) */}
                    <input className="input" placeholder="Mobile Number" />
                    <input className="input" placeholder="Aadhaar Number" />
                    <input className="input" placeholder="PAN Number" />
                    <input className="input" placeholder="Village / District / State" />

                    {role === "farmer" && (
                        <input className="input" placeholder="Land Area (in hectares)" />
                    )}

                    <input type="file" className="w-full text-sm" />

                    <button
                        type="submit"
                        className="w-full bg-green-600 text-white py-2 rounded-lg font-semibold hover:bg-green-700 transition"
                    >
                        Register as {role === "farmer" ? "Farmer" : "Company"}
                    </button>
                </form>

                <p className="text-xs text-center text-gray-500 mt-4">
                    All accounts are verified by admin before activation
                </p>

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
