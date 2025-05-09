import { createBrowserRouter } from "react-router-dom";
import Login from "../login/Login.jsx";
import Register from "../register/Register.jsx";
import Home from "../home/Home.jsx";
import Admin from "../admin/Admin.jsx";
import Setting from "../admin/Setting.jsx";
import AdminDashbord from "../admin/AdminDashbord.jsx";
import ProtectedRoute from "../ProtectedRoute.jsx";
import Ajouter from "../ajouter/Ajouter.jsx";
import Alert from "../admin/Alert.jsx";
import LotPage from "../home/LotPage.jsx";
import Ajoutelog from "../ajouter/AjouteLot.jsx";
import Modifier from "../modifier/modifier.jsx";
import Historique from "../admin/Historique.jsx";
const router = createBrowserRouter([
    {
        path: "/",
        element: <Login />,
    },
    {
        path: "/Register",
        element: <Register />,
    },
    {
        path: "/Admin",
        element: <Admin />,
    },
    {
        path: "/Home",
        element: (
            <ProtectedRoute>
                <Home />
            </ProtectedRoute>
        ),
    },
    {
        path: "/AdminDashbord",
        element: <AdminDashbord />,
    },
    {
        path: "/Ajouter",
        element: <Ajouter/>,
    },
    {
        path: "/Seeting",
        element: <Setting/>,
    },
    {
        path: "/Alert",
        element: <Alert/>,
    },
    {
        path: "/LotAlert",
        element: <LotPage/>,
    },
    {
        path: "/AjouteLot",
        element: <Ajoutelog/>,
    },
    {
        path: "/modifier/:id",
        element: <Modifier/>,
    },
    {
        path: "/historique",
        element: <Historique/>,
    },




]);

export default router;
