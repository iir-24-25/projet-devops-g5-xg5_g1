import logo from '../assets/logo.jpg'
import { Link, useNavigate } from "react-router-dom";
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
                user.passwordHash === password && // attention à ne pas comparer des hash réels comme ça si en prod
                user.role === "ADMINISTRATEUR"
            );

            if (admin) {
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
        <>
            <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
                <div className="sm:mx-auto sm:w-full sm:max-w-sm">
                    <img alt="Your Company" src={logo} className="mx-auto h-10 w-auto" />
                    <h2 className="mt-10 text-center text-2xl/9 font-bold tracking-tight text-gray-900">
                        ADMIN DASHBOARD
                    </h2>
                </div>

                <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
                    <form onSubmit={handleSubmit} className="space-y-6">
                        <div>
                            <label htmlFor="email" className="block text-sm/6 font-medium text-gray-900">
                                Email address
                            </label>
                            <div className="mt-2">
                                <input
                                    id="email"
                                    name="email"
                                    type="email"
                                    required
                                    autoComplete="email"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    className="block border rounded-1xl border-black w-full  bg-white px-3 py-1.5 text-base text-gray-900  placeholder:text-gray-400  sm:text-sm/6"
                                />
                            </div>
                        </div>

                        <div>
                            <label htmlFor="password" className="block text-sm/6 font-medium text-gray-900">
                                Password
                            </label>
                            <div className="mt-2">
                                <input
                                    id="password"
                                    name="password"
                                    type="password"
                                    required
                                    autoComplete="current-password"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    className="block border rounded-1xl border-black w-full  bg-white px-3 py-1.5 text-base text-gray-900  placeholder:text-gray-400  sm:text-sm/6"
                                />
                            </div>
                        </div>

                        <div>
                            <button
                                type="submit"
                                className="flex w-full justify-center rounded-xl bg-black text-white px-3 py-1.5 text-sm/6 font-semibold border-2 hover:bg-white hover:border-black hover:text-black hover:transition "
                            >
                                CONNECT
                            </button>
                        </div>
                        <br />
                    </form>
                </div>
            </div>
        </>
    );
}

export default Admin;
