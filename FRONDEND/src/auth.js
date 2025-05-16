import { initializeApp } from "firebase/app";
import { getAuth, createUserWithEmailAndPassword, signInWithEmailAndPassword, updateProfile } from "firebase/auth";

const firebaseConfig = {
    apiKey: "AIzaSyDrMXQcC7aw8BWOcvCr2sSBbaatBijev8M",
    authDomain: "authpharmacie.firebaseapp.com",
    projectId: "authpharmacie",
    storageBucket: "authpharmacie.appspot.com",
    messagingSenderId: "444784043846",
    appId: "1:444784043846:web:74e13f0b171131f6ed6747",
    measurementId: "G-TW0380PHE6"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);

export const registerPharmacist = async (name, email, password) => {
    const userCredential = await createUserWithEmailAndPassword(auth, email, password);
    const user = userCredential.user;

    // Mettre à jour le profil avec le nom de l'utilisateur
    await updateProfile(user, { displayName: name });

    // Stocker l'ID de l'utilisateur et le nom
    localStorage.setItem("userId", user.uid);  // Stocke l'ID de l'utilisateur
    localStorage.setItem("username", name);  // Stocke le nom
};

export const loginPharmacist = async (email, password) => {
    const userCredential = await signInWithEmailAndPassword(auth, email, password);
    const user = userCredential.user;

    // Stocker l'ID de l'utilisateur et le nom
    localStorage.setItem("userId", user.uid);  // Stocke l'ID de l'utilisateur
    localStorage.setItem("username", user.displayName || "Pharmacist"); // Récupère le nom
};
