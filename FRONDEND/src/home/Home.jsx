import { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import axios from "axios";
import { CirclePlus, Trash2, Pencil } from "lucide-react";
import Sidebar from "@/Sidebar.jsx";
import { Box } from "@mui/material";

export default function Home() {
    const [username, setUsername] = useState("");
    const [medicines, setMedicines] = useState([]);
    const [sidebarVisible, setSidebarVisible] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const storedName = localStorage.getItem("username");
        if (storedName) setUsername(storedName);
        else navigate("/login"); // Redirection si non connecté
    }, [navigate]);

    useEffect(() => {
        const userId = localStorage.getItem("userId");
        axios
            .get(`http://localhost:5050/medicins?userId=${userId}`)
            .then((response) => setMedicines(response.data))
            .catch((error) => console.error("Erreur:", error));
    }, []);

    const handleDelete = (id) => {
        if (window.confirm("Supprimer ce médicament ?")) {
            axios
                .delete(`http://localhost:5050/medicins/${id}`)
                .then(() => setMedicines((prev) => prev.filter((m) => m.id !== id)))
                .catch(() => alert("Suppression échouée"));
        }
    };

    const handleLogout = () => {
        localStorage.clear();
        navigate("/");
    };

    const avatarUrl = `https://ui-avatars.com/api/?name=${encodeURIComponent(username)}&background=random`;

    return (
        <Box sx={{ display: "flex" }}>
            {sidebarVisible && <Sidebar />}
            <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
                <div className="flex justify-between items-center mb-8">
                    <div className="flex items-center space-x-4">
                        <img src={avatarUrl} alt="avatar" className="w-14 h-14 rounded-full shadow" />
                        <div>
                            <p className="text-lg font-bold text-gray-700">Bonjour, Pharmacien {username}</p>
                        </div>
                        <button
                            onClick={() => setSidebarVisible((prev) => !prev)}
                            className="bg-gray-200 text-gray-800 px-3 py-1 rounded-full shadow hover:bg-gray-300 transition"
                        >
                            {sidebarVisible ? "Masquer le menu" : "Afficher le menu"}
                        </button>
                    </div>
                    <div className="flex items-center gap-4">
                        <Link
                            to="/Ajouter"
                            className="flex items-center gap-2 bg-blue-600 text-white px-4 py-2 rounded-full shadow hover:bg-blue-700 transition"
                        >
                            <CirclePlus size={18} />
                            <span className="font-medium">Ajouter un médicament</span>
                        </Link>
                        <button
                            onClick={handleLogout}
                            className="bg-red-500 text-white px-4 py-2 rounded-full shadow hover:bg-red-600 transition"
                        >
                            Déconnexion
                        </button>
                    </div>
                </div>

                {/* Table Card */}
                <div className="bg-white rounded-xl shadow-lg p-6">
                    <h2 className="text-xl font-semibold mb-6 text-gray-800 border-b pb-2">Liste des médicaments</h2>
                    <div className="overflow-x-auto">
                        <table className="min-w-full divide-y divide-gray-200">
                            <thead className="bg-gray-50">
                            <tr>
                                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600">Nom</th>
                                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600">Fabriquant</th>
                                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600">Description</th>
                                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600">Seuil</th>
                                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600">Quantité</th>
                                <th className="px-6 py-3 text-right text-sm font-semibold text-gray-600">Actions</th>
                            </tr>
                            </thead>
                            <tbody className="bg-white divide-y divide-gray-100">
                            {medicines.length === 0 ? (
                                <tr>
                                    <td colSpan="6" className="text-center py-6 text-gray-500">
                                        Aucun médicament trouvé.
                                    </td>
                                </tr>
                            ) : (
                                medicines.map((med) => (
                                    <tr key={med.id} className="hover:bg-gray-50">
                                        <td className="px-6 py-4 font-medium text-gray-800">{med.name}</td>
                                        <td className="px-6 py-4 text-gray-600">{med.fabriquant}</td>
                                        <td className="px-6 py-4 text-gray-600">{med.description}</td>
                                        <td className="px-6 py-4">
                                                <span className="inline-block bg-yellow-100 text-yellow-800 text-xs px-2 py-1 rounded-full">
                                                    {med.seuilAlerte}
                                                </span>
                                        </td>
                                        <td className="px-6 py-4">
                                                <span
                                                    className={`inline-block text-xs px-2 py-1 rounded-full ${
                                                        med.quantity <= med.seuilAlerte
                                                            ? "bg-red-100 text-red-800"
                                                            : "bg-green-100 text-green-800"
                                                    }`}
                                                >
                                                    {med.quantity}
                                                </span>
                                        </td>
                                        <td className="px-6 py-4 flex justify-end gap-3">
                                            <button
                                                className="text-blue-600 hover:text-blue-800"
                                                onClick={() => navigate(`/modifier/${med.id}`)}
                                            >
                                                <Pencil size={18} />
                                            </button>
                                            <button
                                                className="text-red-600 hover:text-red-800"
                                                onClick={() => handleDelete(med.id)}
                                            >
                                                <Trash2 size={18} />
                                            </button>
                                        </td>
                                    </tr>
                                ))
                            )}
                            </tbody>
                        </table>
                    </div>
                </div>
            </Box>
        </Box>
    );
}
