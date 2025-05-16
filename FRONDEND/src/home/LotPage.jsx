import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

const LotPage = () => {
    const [lots, setLots] = useState([]);
    const userId = localStorage.getItem("userId");

    useEffect(() => {
        axios.get(`http://localhost:5050/api/lots?userId=${userId}`)
            .then(res => setLots(res.data))
            .catch(err => console.error(err));
    }, []);

    return (
        <div className="p-6">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-3xl font-bold text-sky-600">Mes Lots</h1>
                <Link to="/AjouteLot" className="bg-green-600 text-white px-4 py-2 rounded">
                    + Nouveau Lot
                </Link>
            </div>

            <div className="overflow-x-auto bg-white shadow-md rounded-lg">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-100">
                    <tr>
                        <th className="px-4 py-2 text-left">Numéro</th>
                        <th className="px-4 py-2 text-left">Médicament</th>
                        <th className="px-4 py-2 text-left">Date Expiration</th>
                        <th className="px-4 py-2 text-left">Quantité</th>
                    </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                    {lots.map((lot) => (
                        <tr key={lot.id}>
                            <td className="px-4 py-2">{lot.numeroLot}</td>
                            <td className="px-4 py-2">{lot.medicin?.name}</td>
                            <td className="px-4 py-2">{new Date(lot.dateExpiration).toLocaleDateString()}</td>
                            <td className="px-4 py-2">{lot.quantite}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default LotPage;
