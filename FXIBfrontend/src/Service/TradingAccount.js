import axios from 'axios';
import {getToken} from './AuthService';

// Axios interceptor to add the Authorization token to all outgoing requests.
axios.interceptors.request.use(
    function (config) {
        config.headers['Authorization'] = getToken();
        return config;
    },
    function (error) {
        return Promise.reject(error);
    }
);

// Function to get all trading accounts.
export const getAllTradingAccounts = () => {
    const url = `http://localhost:8000/accounts`
    return axios.get(url)
        .then((response) => {
            if (response.status === 200) {
                return response.data;
            } else {
                throw new Error('Failed to fetch trading accounts');
            }
        })
        .catch((error) => {
            throw error;
        });
};

// Function to get all trading accounts for the footer.
export const getAllTradingAccountsForFooter =() =>{
    const url = `http://localhost:8000/accounts`
    return axios.put(url)
        .then((response) => {
            if (response.status === 200) {
                return response.data;
            } else {
                throw new Error('Failed to fetch trading accounts');
            }
        })
        .catch((error) => {
            throw error;
        });
}