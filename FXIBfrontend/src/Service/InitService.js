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

// Function to get all data related to the "About" page.
export const getAllAboutData = () => {
    return axios.get(`http://localhost:8000/about`);
};

// Function to get all data related to the "Partners" page.
export const getAllPartnersData = () => {
    return axios.get(`http://localhost:8000/partners`);
};

// Function to get all data related to the "Pricing" page.
export const getAllPricingData = () => {
    return axios.get(`http://localhost:8000/pricing`);
};









