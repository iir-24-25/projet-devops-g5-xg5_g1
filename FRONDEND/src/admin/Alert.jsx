import React, { useEffect, useState } from 'react';
import {
    Settings,
    TriangleAlert,
    LayoutDashboard,
    History,
} from 'lucide-react';
import { Link } from "react-router-dom";
import axios from "axios";

import {
    Table,
    TableBody,
    TableCaption,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";

const Alert = () => {
    const [users, setUsers] = useState([]);
    const [lowStockMedicines, setLowStockMedicines] = useState([]);

    useEffect(() => {
        axios.get("http://localhost:5050/api/users")
            .then(res => setUsers(res.data))
            .catch(err => console.error("Erreur chargement utilisateurs :", err));

        axios.get("http://localhost:5050/medicins/low-stock")
            .then(res => setLowStockMedicines(res.data))
            .catch(err => console.error("Erreur chargement stock faible :", err));
    }, []);

    return (
        <div className="flex min-h-screen bg-gray-100">
            {/* ✅ Sidebar intégrée ici */}
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
                        <Link to="/Alert" className="flex items-center px-4 py-3 rounded-lg bg-sky-100 text-sky-600 font-semibold">
                            <TriangleAlert className="mr-3 w-5 h-5" />
                            <span className="text-sm font-medium">Alertes</span>
                        </Link>
                        <Link to="/historique" className="flex items-center px-4 py-3 rounded-lg text-gray-700 hover:bg-sky-100 hover:text-sky-600 transition">
                            <History className="mr-3 w-5 h-5" />
                            <span className="text-sm font-medium">Historique</span>
                        </Link>
                    </nav>
                </div>
            </aside>

            {/* ✅ Contenu principal décalé à droite grâce à ml-64 */}
            <main className="ml-64 p-8 w-full">
                <h1 className="text-4xl font-bold mb-6">
                    DASHBOARD <span className="text-sky-600">ADMIN</span> - ALERTES
                </h1>

                {/* ✅ Boutons fixes (optionnels) */}


                {/* ✅ Tableau des médicaments en stock faible */}
                {lowStockMedicines.length > 0 ? (
                    <div className="mt-6">
                        <h2 className="text-xl font-semibold text-red-700 mb-4">
                            ⚠️ Médicaments en stock faible
                        </h2>
                        <Table>
                            <TableCaption>Médicaments à surveiller</TableCaption>
                            <TableHeader>
                                <TableRow>
                                    <TableHead>Nom</TableHead>
                                    <TableHead>Quantité</TableHead>
                                    <TableHead>Seuil Alerte</TableHead>
                                    <TableHead>Utilisateur</TableHead>
                                    <TableHead>Email</TableHead>
                                </TableRow>
                            </TableHeader>
                            <TableBody>
                                {lowStockMedicines.map((med) => {
                                    const user = users.find(user => user.uid === med.userId);
                                    return (
                                        <TableRow key={med.id}>
                                            <TableCell>{med.name}</TableCell>
                                            <TableCell>{med.quantity}</TableCell>
                                            <TableCell>{med.seuilAlerte}</TableCell>
                                            <TableCell>{user?.displayName || "Inconnu"}</TableCell>
                                            <TableCell>{user?.email || "Non défini"}</TableCell>
                                        </TableRow>
                                    );
                                })}
                            </TableBody>
                        </Table>
                    </div>
                ) : (
                    <p className="text-gray-500 mt-6">Aucune alerte de stock faible pour le moment.</p>
                )}
            </main>
        </div>
    );
};

export default Alert;