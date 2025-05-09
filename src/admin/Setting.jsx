import React, { useEffect, useState } from "react";
import axios from "axios";
import {
    SunMoon, LayoutDashboard, TriangleAlert,
    History, Settings
} from "lucide-react";
import { Link } from "react-router-dom";

const Seeting = () => {
    const [users, setUsers] = useState([]);
    const [darkMode, setDarkMode] = useState(false); // ÉTAT DU THÈME

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await axios.get("http://localhost:5050/api/users");
                if (Array.isArray(response.data)) {
                    setUsers(response.data);
                } else {
                    console.error("Les données retournées ne sont pas un tableau :", response.data);
                }
            } catch (error) {
                console.error("Erreur lors de la récupération des utilisateurs :", error);
            }
        };

        fetchUsers();
    }, []);

    // 🔄 Active ou désactive le thème sombre
    useEffect(() => {
        if (darkMode) {
            document.documentElement.classList.add("dark");
        } else {
            document.documentElement.classList.remove("dark");
        }
    }, [darkMode]);

    const handleBlockUser = async (uid, block) => {
        const endpoint = block
            ? `http://localhost:5050/firebase/block/${uid}`
            : `http://localhost:5050/firebase/unblock/${uid}`;

        try {
            const response = await axios.post(endpoint);
            alert(response.data);
            setUsers((prevUsers) =>
                prevUsers.map((user) =>
                    user.uid === uid ? { ...user, disabled: block } : user
                )
            );
        } catch (error) {
            console.error("Erreur lors du changement de statut utilisateur :", error);
        }
    };

    return (
        <div className="flex min-h-screen bg-gray-100 dark:bg-gray-900 dark:text-white transition-colors duration-300">
            {/* ✅ Sidebar */}
            <aside className="w-64 bg-white dark:bg-gray-800 shadow-xl border-r border-gray-200 fixed h-full flex flex-col justify-between z-40">
                <div>
                    <div className="px-6 py-6 border-b border-gray-200 dark:border-gray-700">
                        <Link to="/AdminDashbord">
                            <h2 className="text-2xl font-bold text-sky-600 flex items-center">
                                <LayoutDashboard className="mr-2 w-6 h-6" />
                                AdminDashbord
                            </h2>
                        </Link>
                    </div>

                    <nav className="mt-6 space-y-1 px-4">
                        <Link to="/Seeting" className="flex items-center px-4 py-3 rounded-lg bg-sky-100 text-sky-600 font-semibold dark:bg-sky-800 dark:text-white">
                            <Settings className="mr-3 w-5 h-5" />
                            <span className="text-sm font-medium">Manage Users</span>
                        </Link>
                        <Link to="/Alert" className="flex items-center px-4 py-3 rounded-lg text-gray-700 hover:bg-sky-100 hover:text-sky-600 transition dark:text-white dark:hover:bg-sky-800">
                            <TriangleAlert className="mr-3 w-5 h-5" />
                            <span className="text-sm font-medium">Alertes</span>
                        </Link>
                        <Link to="/historique" className="flex items-center px-4 py-3 rounded-lg text-gray-700 hover:bg-sky-100 hover:text-sky-600 transition dark:text-white dark:hover:bg-sky-800">
                            <History className="mr-3 w-5 h-5" />
                            <span className="text-sm font-medium">Historique</span>
                        </Link>
                    </nav>
                </div>
            </aside>

            {/* ✅ Contenu principal */}
            <main className="ml-64 p-8 w-full">
                <div className="flex justify-between items-center mb-6">
                    <h1 className="text-4xl font-bold">
                        DASHBOARD <span className="text-sky-600">ADMIN</span> - GESTION DES UTILISATEURS
                    </h1>

                    {/* ✅ Bouton pour changer de thème */}
                    <button
                        onClick={() => setDarkMode(!darkMode)}
                        className="rounded-full bg-neutral-200 dark:bg-neutral-700 text-black dark:text-white p-2 hover:scale-105 transition"
                        title="Changer de thème"
                    >
                        <SunMoon />
                    </button>
                </div>

                <h3 className="text-lg font-semibold">Liste des Utilisateurs enregistrés</h3>

                <div className="max-h-96 overflow-x-auto mt-4 shadow rounded-lg">
                    <table className="min-w-full divide-y-2 divide-gray-200 dark:divide-gray-700">
                        <thead className="sticky top-0 bg-white dark:bg-gray-800 ltr:text-left rtl:text-right">
                        <tr className="*:font-medium *:text-gray-900 dark:*:text-white">
                            <th className="px-3 py-2 whitespace-nowrap">UID</th>
                            <th className="px-3 py-2 whitespace-nowrap">Nom</th>
                            <th className="px-3 py-2 whitespace-nowrap">Email</th>
                            <th className="px-3 py-2 whitespace-nowrap">Statut</th>
                            <th className="px-3 py-2 whitespace-nowrap">Actions</th>
                        </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-200 dark:divide-gray-700">
                        {users.map((user) => (
                            <tr key={user.uid} className="*:text-gray-900 dark:*:text-white *:first:font-medium">
                                <td className="px-3 py-2 whitespace-nowrap">{user.uid}</td>
                                <td className="px-3 py-2 whitespace-nowrap">{user.displayName}</td>
                                <td className="px-3 py-2 whitespace-nowrap">{user.email}</td>
                                <td className="px-3 py-2 whitespace-nowrap">
                                    {user.disabled ? "Bloqué" : "Actif"}
                                </td>
                                <td className="px-3 py-2 whitespace-nowrap">
                                    <button
                                        className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
                                        onClick={() => handleBlockUser(user.uid, true)}
                                        disabled={user.disabled}
                                    >
                                        Bloquer
                                    </button>
                                    <button
                                        className="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600 ml-2"
                                        onClick={() => handleBlockUser(user.uid, false)}
                                        disabled={!user.disabled}
                                    >
                                        Débloquer
                                    </button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </main>
        </div>
    );
};

export default Seeting;
