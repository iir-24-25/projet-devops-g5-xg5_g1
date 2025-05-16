import logo from '../assets/logo.jpg';
import { useNavigate } from "react-router-dom";
import axios from 'axios';
import { useState } from 'react';

function Admin() {
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const res = await axios.get("http://localhost:5050/admin/all-users");
            const users = res.data;

            const admin = users.find(user =>
                user.email === email &&
                user.passwordHash === password && // ne pas utiliser en prod sans hash
                user.role === "ADMINISTRATEUR"
            );

            if (admin) {
                // Stockage dans localStorage
                localStorage.setItem("username", admin.email);
                localStorage.setItem("role", admin.role);
                navigate('/AdminDashbord');
            } else {
                alert("Identifiants invalides ou non autorisés.");
            }

        } catch (err) {
            console.error("Erreur lors de la récupération des utilisateurs :", err);
            alert("Erreur serveur.");
        }
    };

    return (
        <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
            <div className="sm:mx-auto sm:w-full sm:max-w-sm">
                <img alt="Your Company" src={logo} className="mx-auto h-10 w-auto" />
                <h2 className="mt-10 text-center text-2xl font-bold tracking-tight text-gray-900">
                    ADMIN DASHBOARD
                </h2>
            </div>

            <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
                <form onSubmit={handleSubmit} className="space-y-6">
                    <div>
                        <label htmlFor="email" className="block text-sm font-medium text-gray-900">Email address</label>
                        <input
                            id="email"
                            name="email"
                            type="email"
                            required
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            className="mt-2 block w-full rounded-xl border border-black px-3 py-1.5 text-gray-900"
                        />
                    </div>

                    <div>
                        <label htmlFor="password" className="block text-sm font-medium text-gray-900">Password</label>
                        <input
                            id="password"
                            name="password"
                            type="password"
                            required
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="mt-2 block w-full rounded-xl border border-black px-3 py-1.5 text-gray-900"
                        />
                    </div>

                    <button
                        type="submit"
                        className="flex w-full justify-center rounded-xl bg-black text-white px-3 py-1.5 text-sm font-semibold hover:bg-white hover:text-black hover:border hover:border-black"
                    >
                        CONNECT
                    </button>
                </form>
            </div>
        </div>
    );
}

export default Admin;
