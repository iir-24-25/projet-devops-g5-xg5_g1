import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from "react-router-dom";
import {
    Table, TableBody, TableCell, TableContainer,
    TableHead, TableRow, Paper, Typography, Alert, Box
} from '@mui/material';
import Sidebar from "@/Sidebar.jsx";

const AlertUser = () => {
    const [alerts, setAlerts] = useState([]);
    const [username, setUsername] = useState("");
    const [sidebarVisible, setSidebarVisible] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const storedName = localStorage.getItem("username");
        if (storedName) setUsername(storedName);
        else navigate("/login"); // Rediriger si non connecté
    }, [navigate]);

    useEffect(() => {
        const userId = localStorage.getItem('userId');
        if (!userId) return;

        axios.get(`http://localhost:5050/medicins?userId=${userId}`)
            .then(res => {
                const lowStock = res.data.filter(med => med.quantity <= med.seuilAlerte);
                setAlerts(lowStock);
            })
            .catch(err => console.error("Erreur chargement médicaments :", err));
    }, []);

    const handleLogout = () => {
        localStorage.clear();
        navigate("/");
    };

    const avatarUrl = `https://ui-avatars.com/api/?name=${encodeURIComponent(username)}&background=random`;

    return (
        <Box sx={{ display: "flex" }}>
            {sidebarVisible && <Sidebar />}
            <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
                {/* Header */}
                <div className="flex justify-between items-center mb-8">
                    <div className="flex items-center space-x-4">
                        <img src={avatarUrl} alt="avatar" className="w-14 h-14 rounded-full shadow" />
                        <p className="text-lg font-bold text-gray-700">Bonjour, Pharmacien {username}</p>
                        <button
                            onClick={() => setSidebarVisible(prev => !prev)}
                            className="bg-gray-200 text-gray-800 px-3 py-1 rounded-full shadow hover:bg-gray-300 transition"
                        >
                            {sidebarVisible ? "Masquer le menu" : "Afficher le menu"}
                        </button>
                    </div>
                    <button
                        onClick={handleLogout}
                        className="bg-red-500 text-white px-4 py-2 rounded-full shadow hover:bg-red-600 transition"
                    >
                        Déconnexion
                    </button>
                </div>

                {/* Alert Section */}
                <div className="bg-white rounded-xl shadow-lg p-6">
                    <Typography variant="h5" gutterBottom className="mb-4 text-gray-800 border-b pb-2">
                        ⚠️ Médicaments en stock faible
                    </Typography>

                    {alerts.length === 0 ? (
                        <Alert severity="success">
                            Aucun médicament en stock faible pour le moment.
                        </Alert>
                    ) : (
                        <TableContainer component={Paper}>
                            <Table>
                                <TableHead>
                                    <TableRow>
                                        <TableCell><strong>Nom</strong></TableCell>
                                        <TableCell align="right"><strong>Quantité</strong></TableCell>
                                        <TableCell align="right"><strong>Seuil Alerte</strong></TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {alerts.map((med, index) => (
                                        <TableRow key={index}>
                                            <TableCell>{med.name}</TableCell>
                                            <TableCell align="right">{med.quantity}</TableCell>
                                            <TableCell align="right">{med.seuilAlerte}</TableCell>
                                            <TableCell align="right">{med.categorie}</TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    )}
                </div>
            </Box>
        </Box>
    );
};

export default AlertUser;

