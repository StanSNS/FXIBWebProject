import axios from "axios";
import CryptoJS from 'crypto-js';

// Function to make an API call for user registration.
export const registerAPICall = (registerObj) => axios.post(`http://localhost:8000/auth/register`, registerObj);

// Function to make an API call for user login.
export const loginAPICall = (username, password) => axios.post(`http://localhost:8000/auth/login`, {
    username,
    password
});

// Secret key for encryption and decryption
const SECRET_KEY = "FXIB_SECRET"

const encryptData = (data) => {
    let encJson = CryptoJS.AES.encrypt(JSON.stringify(data), SECRET_KEY).toString();
    return CryptoJS.enc.Base64.stringify(CryptoJS.enc.Utf8.parse(encJson));
}

const decryptData = (data) => {
    let decData = CryptoJS.enc.Base64.parse(data).toString(CryptoJS.enc.Utf8);
    let bytes = CryptoJS.AES.decrypt(decData, SECRET_KEY).toString(CryptoJS.enc.Utf8);
    return JSON.parse(bytes)
}

// Function to store a token in the local storage.
export const storeToken = (token) => localStorage.setItem("token", token);

// Function to retrieve a token from the local storage.
export const getToken = () => (localStorage.getItem("token"));




// Function to save the logged-in user's information in session storage.
export const saveLoggedUser = (username, role, email) => {
    sessionStorage.setItem("Username", encryptData(username));
    sessionStorage.setItem("Roles", encryptData(role));
    sessionStorage.setItem("Email", encryptData(email));
}

// Retrieve the username of the logged-in user from session storage.
export const loggedUserUsername = () => {
    const username = sessionStorage.getItem("Username");
    if (username) {
        return decryptData(username);
    }
}

export const loggedUserEmail = () => {
    const email = sessionStorage.getItem("Email");
    if (email) {
        return decryptData(email);
    }
}

// Function to check if a user is logged in based on session storage.
export const isUserLoggedIn = () => {
    return sessionStorage.getItem("Username") != null;
}


// Function to check if the logged-in user is an administrator based on their role.
export const isAdministrator = () => {
    let role = sessionStorage.getItem("Roles");

    if (role) {
        role = decryptData(role);
        if (role.includes("ADMIN")) {
            return true;
        }
    }
    return false;
}

// Function to check if the logged-in user is banned based on their role.
export const isUserBanned = () => {
    let role = sessionStorage.getItem("Roles");

    if (role) {
        role = decryptData(role);
        if (role.includes("BANNED")) {
            return true;
        }

    }
    return false;
}

// Function to log out the user by clearing both local and session storage.
export const logout = () => {
    localStorage.clear();
    sessionStorage.clear();
};

// Function to perform login with two-factor authentication.
export const loginWith2FACode = (username, password, code) => {
    return axios.post(`http://localhost:8000/auth/login/two-factor-auth?username=${username}&password=${password}&code=${code}`)
};