import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { ArrowLeftFromLine } from 'lucide-react';

export default function Ajouter() {
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        name: '',
        fabriquant: '',
        codeBarres: '',
        description: '',
        seuilAlerte: '',
        quantity: '',
    });

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const userId = localStorage.getItem("userId");
        try {
            await axios.post('http://localhost:5050/medicins', { ...formData, userId });
            navigate('/Home');
        } catch (error) {
            console.error("Erreur lors de l'ajout du produit :", error);
        }
    };

    return (
        <>
            {/* Bouton Retour */}
            <div className="fixed top-4 left-4 z-50">
                <button
                    onClick={() => navigate('/Home')}
                    className="flex items-center gap-2 bg-indigo-700 text-white px-4 py-2 rounded-xl shadow hover:bg-indigo-900 transition"
                >
                    <ArrowLeftFromLine size={18} />
                    Retour
                </button>
            </div>

            {/* Contenu principal */}
            <div className="min-h-screen bg-white px-6 py-24 sm:py-32 lg:px-8">
                <div className="mx-auto max-w-2xl text-center">
                    <h2 className="text-4xl font-semibold tracking-tight text-gray-900 sm:text-5xl">
                        Ajouter un médicament
                    </h2>
                    <p className="mt-2 text-lg text-gray-600">Remplissez les informations du produit</p>
                </div>

                {/* Formulaire */}
                <form onSubmit={handleSubmit} className="mx-auto mt-16 max-w-xl sm:mt-20">
                    <div className="grid grid-cols-1 gap-x-6 gap-y-6 sm:grid-cols-2">
                        {[
                            { label: 'Nom du médicament', name: 'name', type: 'text' },
                            { label: 'Fabriquant', name: 'fabriquant', type: 'text' },
                            { label: 'Code-barres', name: 'codeBarres', type: 'text', full: true },
                            { label: 'Description', name: 'description', type: 'text', full: true },
                            { label: 'Seuil d’alerte', name: 'seuilAlerte', type: 'number', full: true },
                            { label: 'Quantité en stock', name: 'quantity', type: 'number', full: true },
                        ].map(({ label, name, type, full }) => (
                            <div key={name} className={full ? 'sm:col-span-2' : ''}>
                                <label htmlFor={name} className="block text-sm font-medium text-gray-700 mb-1">
                                    {label}
                                </label>
                                <input
                                    id={name}
                                    name={name}
                                    type={type}
                                    value={formData[name]}
                                    onChange={handleChange}
                                    required
                                    className="w-full rounded-xl border border-gray-300 px-4 py-2 text-gray-900 shadow-sm focus:border-indigo-600 focus:ring-2 focus:ring-indigo-200 transition"
                                />
                            </div>
                        ))}
                    </div>

                    <div className="mt-10">
                        <button
                            type="submit"
                            className="w-full rounded-xl bg-indigo-700 px-6 py-3 text-sm font-semibold text-white shadow hover:bg-indigo-900 transition"
                        >
                            Enregistrer le médicament
                        </button>
                    </div>
                </form>
            </div>
        </>
    );
}
