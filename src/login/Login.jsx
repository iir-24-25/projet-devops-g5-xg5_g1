import logo from '../assets/logo.jpg'
import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import { loginPharmacist } from "@/auth.js";
import { Eye, EyeOff } from "lucide-react"; // Assure-toi d'avoir installé lucide-react

export default function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [showPassword, setShowPassword] = useState(false); // État pour afficher/masquer le mot de passe
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            await loginPharmacist(email, password);
            navigate("/home");
        } catch (error) {
            alert(error.message);
        }
    };

    return (
        <div className="flex min-h-full flex-col justify-center px-6 py-12 lg:px-8">
            <div className="sm:mx-auto sm:w-full sm:max-w-sm">
                <img alt="Your Company" src={logo} className="mx-auto h-10 w-auto" />
                <h2 className="mt-10 text-center text-2xl font-bold tracking-tight text-gray-900">
                    Sign in to your account
                </h2>
            </div>

            <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
                <form onSubmit={handleLogin} className="space-y-6">
                    <div>
                        <label className="block text-sm font-medium text-gray-900">Email address</label>
                        <input
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                            className="mt-2 block w-full border rounded-xl px-3 py-1.5"
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-900">Password</label>
                        <div className="mt-2 relative">
                            <input
                                type={showPassword ? "text" : "password"}
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                                className="block w-full border rounded-xl px-3 py-1.5 pr-10"
                            />
                            <div
                                className="absolute inset-y-0 right-0 flex items-center px-3 cursor-pointer"
                                onClick={() => setShowPassword(!showPassword)}
                            >
                                {showPassword ? (
                                    <EyeOff size={20} className="text-gray-500" />
                                ) : (
                                    <Eye size={20} className="text-gray-500" />
                                )}
                            </div>
                        </div>
                    </div>
                    <button
                        type="submit"
                        className="w-full bg-black text-white rounded-xl py-2 font-semibold hover:bg-white hover:text-black hover:border hover:border-black transition"
                    >
                        Sign in
                    </button>
                </form>
                <p className="mt-4 text-sm">
                    <Link to="/register" className="text-black underline font-semibold">I don’t have an account?</Link>
                </p>
            </div>
        </div>
    );
}
