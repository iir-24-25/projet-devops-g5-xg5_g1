import logo from '../assets/logo.jpg';
import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import { loginPharmacist } from "@/auth.js";
import { Eye, EyeOff } from "lucide-react";
import image from '../assets/pha.jpg';
export default function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [showPassword, setShowPassword] = useState(false);
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            await loginPharmacist(email, password);
            navigate("/home");
        } catch (error) {
            alert(error.message);
        }
    };

    return (
        <div className="max-w-screen-xl m-0 sm:m-10 bg-white shadow sm:rounded-lg flex justify-center flex-1">
            <div className="lg:w-1/2 xl:w-5/12 p-6 sm:p-12">
                <div className="mt-12 flex flex-col items-center">
                    <img src={logo} alt="Logo" className="h-12 mb-4" />
                    <h1 className="text-4xl font-medium">Login </h1>

                    <form onSubmit={handleLogin} className="w-full mt-8 max-w-xs mx-auto">
                        <div className="relative mt-6">
                            <input
                                type="email"
                                name="email"
                                id="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                placeholder="Email Address"
                                autoComplete="off"
                                required
                                className="peer mt-2 w-full bg-transparent border-b-2 border-gray-300 px-0 py-1 placeholder-transparent focus:border-gray-500 focus:outline-none"
                            />
                            <label htmlFor="email" className="absolute left-0 top-0 transform -translate-y-1/2 text-sm text-gray-800 transition-all peer-placeholder-shown:top-1/2 peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-500 peer-focus:top-0 peer-focus:text-sm">Email Address</label>
                        </div>

                        <div className="relative mt-6">
                            <input
                                type={showPassword ? "text" : "password"}
                                name="password"
                                id="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                placeholder="Password"
                                required
                                className="peer w-full bg-transparent border-b-2 border-gray-300 py-1 placeholder-transparent focus:border-gray-500 focus:outline-none pr-10"
                            />
                            <label htmlFor="password" className="absolute left-0 top-0 transform -translate-y-1/2 text-sm text-gray-800 transition-all peer-placeholder-shown:top-1/2 peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-500 peer-focus:top-0 peer-focus:text-sm">Password</label>
                            <div className="absolute right-0 top-2 cursor-pointer" onClick={() => setShowPassword(!showPassword)}>
                                {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                            </div>
                        </div>

                        <div className="flex items-center justify-between mt-8">
                            <button
                                type="submit"
                                className="px-8 py-3 bg-black text-white rounded-md"
                            >
                                Log In
                            </button>
                        </div>
                    </form>

                    <div className="flex justify-evenly items-center space-x-2 w-80 mt-6">
                        <span className="bg-gray-300 h-px flex-grow relative top-2"></span>
                        <span className="bg-gray-300 h-px flex-grow relative top-2"></span>
                    </div>

                    {/* Google, Facebook, Email Buttons */}

                </div>
            </div>

            <div className="flex-1  text-center hidden lg:flex">
                <div
                    className="m-12 w-full bg-contain bg-center bg-no-repeat"
                    style={{ backgroundImage: `url(${image})` }}

                ></div>
            </div>
        </div>
    );
}
