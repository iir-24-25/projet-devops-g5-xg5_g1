import logo from '../assets/logo.jpg'
import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import { registerPharmacist } from "@/auth.js";

export default function Register() {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            await registerPharmacist(name, email, password);
            alert("Inscription réussie !");
            navigate("/");
        } catch (error) {
            alert(error.message);
        }
    };

    return (
        <div className="flex min-h-full flex-col justify-center px-6 py-12 lg:px-8">
            <div className="sm:mx-auto sm:w-full sm:max-w-sm">
                <img alt="Your Company" src={logo} className="mx-auto h-10 w-auto" />
                <h2 className="mt-10 text-center text-2xl font-bold tracking-tight text-gray-900">
                    CREATE AN ACCOUNT
                </h2>
            </div>

            <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
                <form onSubmit={handleRegister} className="space-y-6">
                    <div>
                        <label className="block text-sm font-medium text-gray-900">Name</label>
                        <input
                            type="text"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            required
                            className="mt-2 block w-full border rounded-xl px-3 py-1.5"
                        />
                    </div>
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
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                            className="mt-2 block w-full border rounded-xl px-3 py-1.5"
                        />
                    </div>
                    <button
                        type="submit"
                        className="w-full bg-black text-white rounded-xl py-2 font-semibold hover:bg-white hover:text-black hover:border hover:border-black transition"
                    >
                        Register
                    </button>
                </form>
                <p className="mt-4 text-sm">
                    <Link to="/login" className="text-black underline font-semibold">I have an account</Link>
                </p>
            </div>
        </div>
    );
}
