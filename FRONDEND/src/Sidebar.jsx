// components/Sidebar.jsx
import DashboardIcon from '@mui/icons-material/Dashboard';
import Inventory2Icon from '@mui/icons-material/Inventory2';
import HistoryIcon from '@mui/icons-material/History';
import SettingsIcon from '@mui/icons-material/Settings';
import LogoutIcon from '@mui/icons-material/Logout';
import { useNavigate , Link} from 'react-router-dom';

import { useEffect, useState } from "react";

const Sidebar = () => {
    const [username, setUsername] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        const storedName = localStorage.getItem("username");
        if (storedName) setUsername(storedName);
        else navigate("/login"); // Redirection si non connecté
    }, [navigate]);

    const handleNavigation = (path) => {
        navigate(path);
    };

    const handleLogout = () => {
        localStorage.clear();
        navigate("/");
    };

    const avatarUrl = `https://ui-avatars.com/api/?name=${encodeURIComponent(username)}&background=random`;

    return (
        <div className="fixed inset-y-0 left-0 z-50 w-[130px] bg-white shadow-lg">
            <div className="flex flex-col h-full p-4">
                {/* Avatar */}
                <div className="mb-8 flex items-center justify-center">
                    <div className=" p-2 rounded-lg">
                        <img src={avatarUrl} alt="avatar" className="w-14 h-14 rounded-full shadow"/>
                    </div>
                </div>

                {/* Navigation */}
                <nav className="flex-1 space-y-4">
                    <Link
                        to="/dashbord"
                        className="w-full flex flex-col items-center p-3 hover:bg-blue-50 rounded-xl transition-all duration-200"
                    >
                        <DashboardIcon className="text-gray-600 mb-1"/>
                        <span className="text-xs font-medium text-gray-600">Dashboard</span>
                    </Link>

                    <Link
                        to="/home"
                        className="w-full flex flex-col items-center p-3 hover:bg-blue-50 rounded-xl transition-all duration-200"
                    >
                        <Inventory2Icon className="text-gray-600 mb-1"/>
                        <span className="text-xs font-medium text-gray-600">Stock</span>
                    </Link>



                    <Link
                        to="/AlertUser"
                        className="w-full flex flex-col items-center p-3 hover:bg-blue-50 rounded-xl transition-all duration-200"
                    >
                        <SettingsIcon className="text-gray-600 mb-1"/>
                        <span className="text-xs font-medium text-gray-600">Alert</span>
                    </Link>
                </nav>


                {/* Déconnexion */}
                <div className="mt-auto flex justify-center">
                    <button
                        onClick={handleLogout}
                        className="flex flex-col items-center p-3 hover:bg-red-50 rounded-xl transition-all duration-200"
                    >
                        <LogoutIcon className="text-red-500 mb-1"/>
                        <button className="text-xs font-medium text-red-500" onClick={handleLogout}>Déconnexion</button>
                    </button>
                </div>
            </div>
        </div>
    );
};

export default Sidebar;
