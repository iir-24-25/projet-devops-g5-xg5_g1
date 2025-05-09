import { initializeApp, getApps } from "firebase/app"; // 👈 ajoute getApps
import { getAuth } from "firebase/auth";
import { getAnalytics } from "firebase/analytics";
import { getDatabase } from "firebase/database";

const firebaseConfig = {
    apiKey: "AIzaSyDrMXQcC7aw8BWOcvCr2sSBbaatBijev8M",
    authDomain: "authpharmacie.firebaseapp.com",
    projectId: "authpharmacie",
    storageBucket: "authpharmacie.firebasestorage.app",
    messagingSenderId: "444784043846",
    appId: "1:444784043846:web:74e13f0b171131f6ed6747",
    measurementId: "G-TW0380PHE6",
    databaseURL: "https://authpharmacie-default-rtdb.firebaseio.com/"
};

// ✅ CORRECTION
const app = getApps().length === 0 ? initializeApp(firebaseConfig) : getApps()[0];

const analytics = getAnalytics(app);

export const auth = getAuth(app);
export const database = getDatabase(app);
