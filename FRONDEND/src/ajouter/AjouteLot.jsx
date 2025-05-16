import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const AjouteLot = () => {
    const navigate = useNavigate();
    const [medicins, setMedicins] = useState([]);
    const [formData, setFormData] = useState({
        numeroLot: '',
        dateExpiration: '',
        quantite: '',
        medicinId: ''
    });

    useEffect(() => {
        const userId = localStorage.getItem("userId");
        axios.get(`http://localhost:5050/medicins?userId=${userId}`)
            .then(res => setMedicins(res.data))
            .catch(err => console.error(err));
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        const userId = localStorage.getItem("userId");

        try {
            await axios.post("http://localhost:5050/api/lots", {
                ...formData,
                userId
            });
            navigate("/LotPage");
        } catch (err) {
            console.error(err);
        }
    };

    return (
        <div className="p-6 max-w-xl mx-auto">
            <h2 className="text-2xl font-bold mb-4">Ajouter un lot</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
                <select
                    required
                    value={formData.medicinId}
                    onChange={(e) => setFormData({ ...formData, medicinId: e.target.value })}
                    className="w-full p-2 border rounded"
                >
                    <option value="">Choisir un médicament</option>
                    {medicins.map((med) => (
                        <option key={med.id} value={med.id}>
                            {med.name} ({med.fabriquant})
                        </option>
                    ))}
                </select>

                <input
                    type="text"
                    placeholder="Numéro de lot"
                    required
                    className="w-full p-2 border rounded"
                    onChange={(e) => setFormData({ ...formData, numeroLot: e.target.value })}
                />

                <input
                    type="date"
                    required
                    className="w-full p-2 border rounded"
                    onChange={(e) => setFormData({ ...formData, dateExpiration: e.target.value })}
                />

                <input
                    type="number"
                    placeholder="Quantité"
                    required
                    className="w-full p-2 border rounded"
                    onChange={(e) => setFormData({ ...formData, quantite: e.target.value })}
                />

                <button
                    type="submit"
                    className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
                >
                    Ajouter
                </button>
            </form>
        </div>
    );
};

export default AjouteLot;
