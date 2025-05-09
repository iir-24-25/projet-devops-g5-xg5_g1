import React, { useEffect, useState } from 'react';
import axios from 'axios';
import {
    LayoutDashboard,
    TriangleAlert,
    History,
    Settings
} from 'lucide-react';
import { Link } from "react-router-dom";

const HistoriquePage = () => {
    const [historiques, setHistoriques] = useState([]);
    const [users, setUsers] = useState([]);

    // Récupérer historique et utilisateurs
    useEffect(() => {
        axios.get('http://localhost:5050/api/historique')
            .then(response => setHistoriques(response.data))
            .catch(error => console.error("Erreur lors du chargement de l'historique :", error));

        axios.get('http://localhost:5050/api/users')
            .then(res => setUsers(res.data))
            .catch(err => console.error("Erreur lors du chargement des utilisateurs :", err));
    }, []);

    return (
        <div className="flex min-h-screen bg-gray-100">
            {/* ✅ Sidebar intégrée */}
            <aside className="w-64 bg-white shadow-xl border-r border-gray-200 fixed h-full flex flex-col justify-between z-40">
                <div>
                    <div className="px-6 py-6 border-b border-gray-200">
                        <Link to="/AdminDashbord">
                            <h2 className="text-2xl font-bold text-sky-600 flex items-center">
                                <LayoutDashboard className="mr-2 w-6 h-6" />
                                AdminDashbord
                            </h2>
                        </Link>
                    </div>

                    <nav className="mt-6 space-y-1 px-4">
                        <Link to="/Seeting" className="flex items-center px-4 py-3 rounded-lg text-gray-700 hover:bg-sky-100 hover:text-sky-600 transition">
                            <Settings className="mr-3 w-5 h-5" />
                            <span className="text-sm font-medium">Manage Users</span>
                        </Link>
                        <Link to="/Alert" className="flex items-center px-4 py-3 rounded-lg text-gray-700 hover:bg-sky-100 hover:text-sky-600 transition">
                            <TriangleAlert className="mr-3 w-5 h-5" />
                            <span className="text-sm font-medium">Alertes</span>
                        </Link>
                        <Link to="/historique" className="flex items-center px-4 py-3 rounded-lg bg-sky-100 text-sky-600 font-semibold">
                            <History className="mr-3 w-5 h-5" />
                            <span className="text-sm font-medium">Historique</span>
                        </Link>
                    </nav>
                </div>
            </aside>

            {/* ✅ Contenu principal décalé à droite grâce à ml-64 */}
            <main className="ml-64 p-8 w-full">
                <h2 className="text-3xl font-semibold text-gray-800 mb-6">🕒 Historique des Médicaments</h2>
                <div className="overflow-x-auto">
                    <table className="min-w-full table-auto">
                        <thead className="bg-gray-200 text-gray-600 uppercase text-sm leading-normal">
                        <tr>
                            <th className="py-3 px-6 text-left">Action</th>
                            <th className="py-3 px-6 text-left">Nom du médicament</th>
                            <th className="py-3 px-6 text-left">Date</th>
                            <th className="py-3 px-6 text-left">Email utilisateur</th>
                        </tr>
                        </thead>
                        <tbody className="text-gray-700 text-sm font-light">
                        {historiques.length > 0 ? (
                            historiques.map((histo, index) => {
                                const user = users.find(u => u.uid === histo.userId);
                                return (
                                    <tr key={index} className="border-b border-gray-200 hover:bg-gray-100">
                                        <td className="py-3 px-6 text-left capitalize font-semibold">{histo.action}</td>
                                        <td className="py-3 px-6 text-left">{histo.medicinName}</td>
                                        <td className="py-3 px-6 text-left">
                                            {new Date(histo.dateAction).toLocaleString()}
                                        </td>
                                        <td className="py-3 px-6 text-left">{user?.email || "Inconnu"}</td>
                                    </tr>
                                );
                            })
                        ) : (
                            <tr>
                                <td colSpan="4" className="text-center py-6">Aucun historique trouvé.</td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                </div>
            </main>
        </div>
    );
};

export default HistoriquePage;