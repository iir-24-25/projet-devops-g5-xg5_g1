import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";

export default function Modifier() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [form, setForm] = useState({
        name: "",
        fabriquant: "",
        description: "",
        seuilAlerte: "",
        quantity: "",
    });

    useEffect(() => {
        axios.get(`http://localhost:5050/medicins/${id}`)
            .then((res) => setForm(res.data))
            .catch((err) => {
                console.error("Erreur lors de la récupération :", err);
                alert("Erreur lors du chargement du médicament.");
            });
    }, [id]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setForm((prev) => ({ ...prev, [name]: value }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        axios.put(`http://localhost:5050/medicins/${id}`, form)
            .then(() => {
                alert("Médicament modifié avec succès !");
                navigate("/home");
            })
            .catch((err) => {
                console.error("Erreur lors de la mise à jour :", err);
                alert("Erreur lors de la modification.");
            });
    };

    return (
        <div className="max-w-xl mx-auto mt-20 p-6 bg-white border rounded shadow">
            <h2 className="text-2xl font-bold mb-6 text-center">Modifier Médicament</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
                <input
                    type="text"
                    name="name"
                    value={form.name}
                    onChange={handleChange}
                    placeholder="Nom"
                    className="w-full border p-2 rounded"
                    required
                />
                <input
                    type="text"
                    name="fabriquant"
                    value={form.fabriquant}
                    onChange={handleChange}
                    placeholder="Fabriquant"
                    className="w-full border p-2 rounded"
                />
                <textarea
                    name="description"
                    value={form.description}
                    onChange={handleChange}
                    placeholder="Description"
                    className="w-full border p-2 rounded"
                />
                <input
                    type="number"
                    name="seuilAlerte"
                    value={form.seuilAlerte}
                    onChange={handleChange}
                    placeholder="Seuil d'alerte"
                    className="w-full border p-2 rounded"
                />
                <input
                    type="number"
                    name="quantity"
                    value={form.quantity}
                    onChange={handleChange}
                    placeholder="Quantité"
                    className="w-full border p-2 rounded"
                />
                <button
                    type="submit"
                    className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700"
                >
                    Enregistrer les modifications
                </button>
            </form>
        </div>
    );
}
