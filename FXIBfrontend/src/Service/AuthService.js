import axios from "axios";

// Function to make an API call for user registration.
export const registerAPICall = (registerObj) => axios.post(`http://localhost:8000/auth/register`, registerObj);

// Function to make an API call for user login.
export const loginAPICall = (username, password) => axios.post(`http://localhost:8000/auth/login`, {
    username,
    password
});

// Function to store a token in the local storage.
export const storeToken = (token) => localStorage.setItem("token", token);

// Function to retrieve a token from the local storage.
export const getToken = () => localStorage.getItem("token");

// Function to save the logged-in user's information in session storage.
export const saveLoggedUser = (username, role) => {
    sessionStorage.setItem("authenticatedUser", username);
    sessionStorage.setItem("role", role);
}

// Retrieve the username of the logged-in user from session storage.
export const loggedUserUsername = sessionStorage.getItem("authenticatedUser");

// Function to check if a user is logged in based on session storage.
export const isUserLoggedIn = () => {
    return sessionStorage.getItem("authenticatedUser") != null;
}

// Function to log out the user by clearing both local and session storage.
export const logout = () => {
    localStorage.clear();
    sessionStorage.clear();
};

// Function to check if the logged-in user is an administrator based on their role.
export const isAdministrator = () => {
    let role = sessionStorage.getItem("role");
    if (role && role.length > 4) {
        let rolesArray = role.split(",");
        if (rolesArray.includes("ADMIN")) {
            return true;
        }
    }
    return false;
}

// Function to check if the logged-in user is banned based on their role.
export const isUserBanned = () => {
    let role = sessionStorage.getItem("role");
    if (role && role.length > 4) {
        let rolesArray = role.split(",");
        if (rolesArray.includes("BANNED")) {
            return true;
        }
    }
    return false;
}

// Function to perform login with two-factor authentication.
export const loginWith2FACode = (username, password, code) => {
    return axios.post(`http://localhost:8000/auth/login/two-factor-auth?username=${username}&password=${password}&code=${code}`)
};



