import React, { useEffect, useState } from "react";
import {
    Typography,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper
} from "@mui/material";
import axios from "axios";

const Histo = () => {
    const [historiques, setHistoriques] = useState([]);
    const [user, setUser] = useState(null); // utilisateur connecté

    useEffect(() => {
        // Récupérer l'utilisateur connecté depuis localStorage
        const storedUser = JSON.parse(localStorage.getItem("user"));
        if (storedUser) {
            setUser(storedUser);
        }

        // Récupérer tous les historiques
        axios.get("http://localhost:5050/api/historique")
            .then(response => setHistoriques(response.data))
            .catch(error => console.error("Erreur lors du chargement de l'historique :", error));
    }, []);

    // Filtrer les logs du user connecté
    const filteredLogs = historiques.filter(
        log => log.userId === user?.uid || log.userId === user?.id
    );

    return (
        <div style={{ padding: "20px" }}>
            <Typography variant="h5" gutterBottom>
                Mon historique
            </Typography>

            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow sx={{ backgroundColor: "#f0f0f0" }}>
                            <TableCell><strong>Action</strong></TableCell>
                            <TableCell><strong>Date</strong></TableCell>
                            <TableCell><strong>Heure</strong></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {filteredLogs.length > 0 ? (
                            filteredLogs.map((log, index) => (
                                <TableRow key={index}>
                                    <TableCell>{log.action}</TableCell>
                                    <TableCell>{new Date(log.timestamp).toLocaleDateString()}</TableCell>
                                    <TableCell>{new Date(log.timestamp).toLocaleTimeString()}</TableCell>
                                </TableRow>
                            ))
                        ) : (
                            <TableRow>
                                <TableCell colSpan={3} align="center">
                                    Aucun historique disponible pour cet utilisateur.
                                </TableCell>
                            </TableRow>
                        )}
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    );
};

export default Histo;
