import React, { useEffect, useState } from "react";
import axios from "axios";
import {
    Box,
    Grid,
    Paper,
    Typography,
    Avatar,
    Alert
} from "@mui/material";
import {
    BarChart,
    Bar,
    PieChart,
    Pie,
    LineChart,
    Line,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    Legend,
    ResponsiveContainer,
    Cell,
    LabelList
} from 'recharts';
import Inventory2Icon from '@mui/icons-material/Inventory2';
import WarningIcon from '@mui/icons-material/Warning';
import SyncAltIcon from '@mui/icons-material/SyncAlt';
import DashboardIcon from '@mui/icons-material/Dashboard';
import HistoryIcon from '@mui/icons-material/History';
import SettingsIcon from '@mui/icons-material/Settings';
import { Link, useNavigate } from "react-router-dom";

const Dashpha = () => {
    const [medicines, setMedicines] = useState([]);
    const [historiques, setHistoriques] = useState([]);
    const [users, setUsers] = useState([]);
    const [username, setUsername] = useState(""); // ✅ Ajouté

    const navigate = useNavigate(); // ✅ Déclaré ici

    useEffect(() => {
        const userId = localStorage.getItem("userId");

        if (userId) {
            axios.get(`http://localhost:5050/medicins?userId=${userId}`)
                .then((res) => setMedicines(res.data))
                .catch((err) => console.error("Erreur récupération:", err));

            axios.get('http://localhost:5050/api/historique')
                .then(response => {
                    const userHistorique = response.data.filter(h => h.userId === userId);
                    setHistoriques(userHistorique);
                })
                .catch(error => console.error("Erreur lors du chargement de l'historique :", error));
        }

        axios.get('http://localhost:5050/api/users')
            .then(res => setUsers(res.data))
            .catch(err => console.error("Erreur lors du chargement des utilisateurs :", err));
    }, []);

    useEffect(() => {
        const storedName = localStorage.getItem("username");
        if (storedName) setUsername(storedName);
        else navigate("/login"); // ✅ Redirection si non connecté
    }, [navigate]);

    const total = medicines.length;
    const lowStock = medicines.filter(m => m.quantity <= m.seuilAlerte).length;
    const mouvements = historiques.length;

    const stockData = [
        { name: 'Stock Faible', value: lowStock },
        { name: 'Stock Suffisant', value: total - lowStock }
    ];
    const COLORS = ['#ff6b6b', '#4dabf7'];

    const mouvementData = historiques.map(h => ({
        date: new Date(h.date).toLocaleDateString(),
        count: 1
    })).reduce((acc, curr) => {
        const exist = acc.find(item => item.date === curr.date);
        if (exist) exist.count += 1;
        else acc.push(curr);
        return acc;
    }, []);

    const avatarUrl = `https://ui-avatars.com/api/?name=${encodeURIComponent(username)}&background=random`;

    return (
        <div className="relative">
            {/* Sidebar */}
            <div className="fixed inset-y-0 left-0 z-50 w-[130px] bg-white shadow-lg">
                <div className="flex flex-col h-full p-4">
                    <div className="mb-8 flex items-center justify-center">
                        <div className="p-2 rounded-lg">
                            <img src={avatarUrl} alt="avatar" className="w-14 h-14 rounded-full shadow" />
                        </div>
                    </div>
                    <nav className="flex-1 space-y-4">
                        <Link to="/dashbord" className="w-full flex flex-col items-center p-3 hover:bg-blue-50 rounded-xl transition-all duration-200">
                            <DashboardIcon className="text-gray-600 mb-1" />
                            <span className="text-xs font-medium text-gray-600">Dashboard</span>
                        </Link>
                        <Link to="/home" className="w-full flex flex-col items-center p-3 hover:bg-blue-50 rounded-xl transition-all duration-200">
                            <Inventory2Icon className="text-gray-600 mb-1" />
                            <span className="text-xs font-medium text-gray-600">Stock</span>
                        </Link>
                        <Link to="/histo" className="w-full flex flex-col items-center p-3 hover:bg-blue-50 rounded-xl transition-all duration-200">
                            <HistoryIcon className="text-gray-600 mb-1" />
                            <span className="text-xs font-medium text-gray-600">Historique</span>
                        </Link>
                        <Link to="/AlertUser" className="w-full flex flex-col items-center p-3 hover:bg-blue-50 rounded-xl transition-all duration-200">
                            <SettingsIcon className="text-gray-600 mb-1" />
                            <span className="text-xs font-medium text-gray-600">Alert</span>
                        </Link>
                    </nav>
                </div>
            </div>

            {/* Main Content */}
            <div className="ml-[130px]">
                <div className="relative isolate flex items-center gap-x-6 overflow-hidden bg-gray-50 px-6 py-2.5">
                    <div className="flex flex-wrap items-center gap-x-4 gap-y-2">
                        <p className="text-sm/6 text-gray-900">
                            <strong className="font-semibold">Tableau de bord Pharmacien</strong>
                        </p>
                    </div>
                </div>

                <Box sx={{ p: 4 }}>
                    <Typography variant="h5" gutterBottom>
                        Tableau de bord Pharmacien
                    </Typography>

                    {lowStock > 0 && (
                        <Alert severity="warning" sx={{ mb: 3 }}>
                            ⚠️ Attention : {lowStock} médicament(s) sont en stock faible !
                        </Alert>
                    )}

                    <Grid container spacing={3}>
                        <Grid item xs={12} md={4}>
                            <Paper elevation={3} sx={{ p: 3, display: "flex", alignItems: "center", borderRadius: 3 }}>
                                <Avatar sx={{ bgcolor: "primary.main", mr: 2 }}>
                                    <Inventory2Icon />
                                </Avatar>
                                <Box>
                                    <Typography variant="subtitle1" color="textSecondary">Total Médicaments</Typography>
                                    <Typography variant="h6">{total}</Typography>
                                </Box>
                            </Paper>
                        </Grid>

                        <Grid item xs={12} md={4}>
                            <Paper elevation={3} sx={{ p: 3, display: "flex", alignItems: "center", borderRadius: 3 }}>
                                <Avatar sx={{ bgcolor: "warning.main", mr: 2 }}>
                                    <WarningIcon />
                                </Avatar>
                                <Box>
                                    <Typography variant="subtitle1" color="textSecondary">Médicaments en stock faible</Typography>
                                    <Typography variant="h6">{lowStock}</Typography>
                                </Box>
                            </Paper>
                        </Grid>

                        <Grid item xs={12} md={4}>
                            <Paper elevation={3} sx={{ p: 3, display: "flex", alignItems: "center", borderRadius: 3 }}>
                                <Avatar sx={{ bgcolor: "info.main", mr: 2 }}>
                                    <SyncAltIcon />
                                </Avatar>
                                <Box>
                                    <Typography variant="subtitle1" color="textSecondary">Mouvements des Médicaments</Typography>
                                    <Typography variant="h6">{mouvements}</Typography>
                                </Box>
                            </Paper>
                        </Grid>

                        {/* Graphiques */}
                        <Grid container spacing={15} sx={{ mt: 3 }}>
                            <Grid item xs={12} md={4}>
                                <Paper elevation={3} sx={{ p: 2, height: 300 }}>
                                    <Typography variant="subtitle1" gutterBottom>Stock des médicaments</Typography>
                                    <ResponsiveContainer width="100%" height="80%">
                                        <BarChart data={medicines}>
                                            <CartesianGrid strokeDasharray="3 3" stroke="#e0e0e0" />
                                            <XAxis dataKey="name" tick={{ fontSize: 10 }} angle={-35} textAnchor="end" interval={0} />
                                            <YAxis />
                                            <Tooltip />
                                            <Legend />
                                            <Bar dataKey="quantity" fill="#1976d2" radius={[4, 4, 0, 0]}>
                                                <LabelList dataKey="quantity" position="top" fill="#333" />
                                            </Bar>
                                        </BarChart>
                                    </ResponsiveContainer>
                                </Paper>
                            </Grid>

                            <Grid item xs={12} md={4}>
                                <Paper elevation={3} sx={{ p: 2, height: 300 }}>
                                    <Typography variant="subtitle1" gutterBottom>Répartition des alertes</Typography>
                                    <ResponsiveContainer width="100%" height="80%">
                                        <PieChart>
                                            <Pie data={stockData} innerRadius={60} outerRadius={80} paddingAngle={5} dataKey="value">
                                                {stockData.map((entry, index) => (
                                                    <Cell key={index} fill={COLORS[index % COLORS.length]} />
                                                ))}
                                            </Pie>
                                            <Tooltip />
                                            <Legend />
                                        </PieChart>
                                    </ResponsiveContainer>
                                </Paper>
                            </Grid>

                            <Grid item xs={12} md={4}>
                                <Paper elevation={3} sx={{ p: 2, height: 300 }}>
                                    <Typography variant="subtitle1" gutterBottom>Tendances des mouvements</Typography>
                                    <ResponsiveContainer width="100%" height="80%">
                                        <LineChart data={mouvementData}>
                                            <CartesianGrid strokeDasharray="3 3" stroke="#e0e0e0" />
                                            <XAxis dataKey="date" tick={{ fontSize: 11 }} />
                                            <YAxis />
                                            <Tooltip />
                                            <Legend />
                                            <Line type="monotone" dataKey="count" stroke="#ff7043" strokeWidth={3} dot={{ r: 4 }} activeDot={{ r: 6 }} />
                                        </LineChart>
                                    </ResponsiveContainer>
                                </Paper>
                            </Grid>
                        </Grid>
                    </Grid>
                </Box>
            </div>
        </div>
    );
};

export default Dashpha;
