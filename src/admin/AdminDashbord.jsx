import React, { useEffect, useState } from 'react';
import {
    Settings, TriangleAlert, Pill, CircleAlert,
    User, History, FileText, LayoutDashboard
} from 'lucide-react';
import { Link } from "react-router-dom";
import axios from "axios";

import { Bar, Doughnut, Pie } from 'react-chartjs-2';
import {
    Chart as ChartJS,
    ArcElement,
    BarElement,
    CategoryScale,
    LinearScale,
    Tooltip,
    Legend
} from 'chart.js';
import { Button } from "@/components/ui/button.js";

import jsPDF from "jspdf";
import autoTable from "jspdf-autotable";
import * as XLSX from "xlsx";
import { saveAs } from "file-saver";

ChartJS.register(ArcElement, BarElement, CategoryScale, LinearScale, Tooltip, Legend);

const AdminDashbord = () => {
    const [users, setUsers] = useState([]);
    const [medicines, setMedicines] = useState([]);
    const [lowStockMedicines, setLowStockMedicines] = useState([]);

    useEffect(() => {
        axios.get("http://localhost:5050/api/users")
            .then(res => setUsers(res.data))
            .catch(err => console.error("Erreur utilisateurs :", err));

        axios.get("http://localhost:5050/medicins")
            .then(res => setMedicines(res.data))
            .catch(err => console.error("Erreur médicaments :", err));

        axios.get("http://localhost:5050/medicins/low-stock")
            .then(res => setLowStockMedicines(res.data))
            .catch(err => console.error("Erreur stock faible :", err));
    }, []);

    const totalMedicines = medicines.length;
    const alertMedicines = lowStockMedicines.length;
    const totalUsers = users.length;

    // ✅ Export PDF
    const exportMedicinesToPDF = () => {
        const doc = new jsPDF();
        doc.text("Liste des Médicaments", 14, 10);
        autoTable(doc, {
            head: [["Nom", "Quantité", "Description"]],
            body: medicines.map(m => [m.name, m.quantity, m.description || ""]),
        });
        doc.save("medicaments.pdf");
    };

    // ✅ Export Excel
    const exportMedicinesToExcel = () => {
        const worksheet = XLSX.utils.json_to_sheet(
            medicines.map(m => ({
                Nom: m.name,
                Quantité: m.quantity,
                Description: m.description || "",
            }))
        );
        const workbook = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(workbook, worksheet, "Médicaments");
        const excelBuffer = XLSX.write(workbook, { bookType: "xlsx", type: "array" });
        const data = new Blob([excelBuffer], { type: "application/octet-stream" });
        saveAs(data, "medicaments.xlsx");
    };

    return (
        <div className="flex min-h-screen bg-gray-100">
            {/* ✅ Sidebar */}
            <aside
                className="w-64 bg-white shadow-xl border-r border-gray-200 fixed h-full flex flex-col justify-between">
                <div>
                    <div className="px-6 py-6 border-b border-gray-200">
                        <h2 className="text-2xl font-bold text-sky-600 flex items-center">
                            <LayoutDashboard className="mr-2 w-10 h-10"/>
                            AdminDashbord
                        </h2>

                    </div>

                    <nav className="mt-6 space-y-1 px-4">
                        <Link
                            to="/Seeting"
                            className="flex items-center px-4 py-3 rounded-lg text-gray-700 hover:bg-sky-100 hover:text-sky-600 transition"
                        >
                            <Settings className="mr-3 w-5 h-5"/>
                            <span className="text-sm font-medium">Manage Users</span>
                        </Link>
                        <Link
                            to="/Alert"
                            className="flex items-center px-4 py-3 rounded-lg text-gray-700 hover:bg-sky-100 hover:text-sky-600 transition"
                        >
                            <TriangleAlert className="mr-3 w-5 h-5"/>
                            <span className="text-sm font-medium">Alertes</span>
                        </Link>
                        <Link
                            to="/historique"
                            className="flex items-center px-4 py-3 rounded-lg text-gray-700 hover:bg-sky-100 hover:text-sky-600 transition"
                        >
                            <History className="mr-3 w-5 h-5"/>
                            <span className="text-sm font-medium">Historique</span>
                        </Link>
                    </nav>
                </div>

                <div className="px-4 py-4 border-t border-gray-200 space-y-2">
                    <Button
                        onClick={exportMedicinesToPDF}
                        variant="outline"
                        className="w-full flex items-center justify-start space-x-3 text-gray-700 hover:text-red-600"
                    >
                        <FileText className="w-5 h-5"/>
                        <span>Exporter en PDF</span>
                    </Button>
                    <Button
                        onClick={exportMedicinesToExcel}
                        variant="outline"
                        className="w-full flex items-center justify-start space-x-3 text-gray-700 hover:text-green-600"
                    >
                        <History className="w-5 h-5"/>
                        <span>Exporter en Excel</span>
                    </Button>
                </div>
            </aside>

            {/* ✅ Contenu principal */}
            <main className="ml-64 p-8 w-full">
                <h1 className="text-4xl font-bold mb-6">Tableau de bord <span className="text-sky-600">Admin</span></h1>

                {/* ✅ Statistiques */}
                <div className="grid grid-cols-1 sm:grid-cols-3 gap-5 mb-10">
                    <div className="bg-white shadow-md rounded-2xl p-4 flex items-center justify-between">
                        <div>
                            <p className="text-gray-500 text-sm">Total Médicaments</p>
                            <p className="text-3xl font-bold text-sky-600">{totalMedicines}</p>
                        </div>
                        <Pill className="text-sky-600 w-10 h-10"/>
                    </div>
                    <div className="bg-white shadow-md rounded-2xl p-4 flex items-center justify-between">
                        <div>
                            <p className="text-gray-500 text-sm">En Alerte</p>
                            <p className="text-3xl font-bold text-red-600">{alertMedicines}</p>
                        </div>
                        <CircleAlert className="text-red-600 w-10 h-10"/>
                    </div>
                    <div className="bg-white shadow-md rounded-2xl p-4 flex items-center justify-between">
                        <div>
                            <p className="text-gray-500 text-sm">Utilisateurs</p>
                            <p className="text-3xl font-bold text-green-600">{totalUsers}</p>
                        </div>
                        <User className="text-green-600 w-10 h-10"/>
                    </div>
                </div>

                {/* ✅ Graphiques */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                    <div className="bg-white p-4 rounded-xl shadow-md">
                        <h2 className="text-lg font-semibold mb-4 text-gray-700">Total Médicaments</h2>
                        <Bar
                            data={{
                                labels: ['Médicaments'],
                                datasets: [{
                                    label: 'Total',
                                    data: [totalMedicines],
                                    backgroundColor: '#3b82f6'
                                }]
                            }}
                            options={{
                                responsive: true,
                                plugins: {legend: {display: false}}
                            }}
                        />
                    </div>

                    <div className="bg-white p-4 rounded-xl shadow-md">
                        <h2 className="text-lg font-semibold mb-4 text-gray-700">En Alerte</h2>
                        <Doughnut
                            data={{
                                labels: ['En Alerte', 'Normaux'],
                                datasets: [{
                                    data: [alertMedicines, totalMedicines - alertMedicines],
                                    backgroundColor: ['#ef4444', '#22c55e']
                                }]
                            }}
                            options={{responsive: true}}
                        />
                    </div>

                    <div className="bg-white p-4 rounded-xl shadow-md">
                        <h2 className="text-lg font-semibold mb-4 text-gray-700">Utilisateurs</h2>
                        <Pie
                            data={{
                                labels: ['Utilisateurs'],
                                datasets: [{
                                    data: [totalUsers],
                                    backgroundColor: ['#10b981']
                                }]
                            }}
                            options={{responsive: true}}
                        />
                    </div>
                </div>
            </main>
        </div>
    );
};

export default AdminDashbord;
