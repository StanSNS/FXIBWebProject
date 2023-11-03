import axios from "axios";
import {getToken} from "./AuthService";

// Axios interceptor to add the Authorization token to all outgoing requests.
axios.interceptors.request.use(
    function (config) {
        // Set the 'Authorization' header in the request with the token obtained from AuthService.
        config.headers['Authorization'] = getToken();
        return config;
    },
    function (error) {
        return Promise.reject(error);
    }
);

// Function to get all users based on the provided username and JWT token.
export const getAllUsers = (username, jwtToken) => {
    return axios.get(`http://localhost:8000/admin?username=${username}&jwtToken=${jwtToken}`)
        .then((response) => {
            if (response.status === 200) {
                return response.data;
            } else {
                throw new Error('Failed to get all users');
            }
        })
        .catch((error) => {
            throw error;
        });
};

// Function to update the roles of a user.
export const updateBlockedUserRoles = (loggedUsername,jwtToken, banUsername, roles) => {
    return axios.put(`http://localhost:8000/admin?loggedUsername=${loggedUsername}&jwtToken=${jwtToken}&banUsername=${banUsername}`, roles)
        .then((response) => {
            if (response.status === 200) {
                return response.data;
            } else {
                throw new Error('Failed to block the user');
            }
        }).catch((error) => {
            throw error;
        });
};
