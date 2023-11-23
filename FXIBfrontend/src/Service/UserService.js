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

// Function to update a user's password using a reset token and a new password.
export const updateResetPasswordFromEmail = (resetToken, newPassword) => {
    const url = `http://localhost:8000/reset-password-update?resetToken=${resetToken}&newPassword=${newPassword}`
    return axios.put(url)
        .then((response) => {
            if (response.status === 200) {
                return response.data
            } else {
                throw new Error('Failed to update user biography');
            }
        }).catch((error) => {
            throw error;
        });
};

// Function to initiate a user password reset by sending an email to the specified email address.
export const userResetPassword = (toEmail) => {
    const url = `http://localhost:8000/reset-password?toEmail=${toEmail}`
    return axios.post(url)
        .then((response) => {
            if (response.status === 200) {
                return response.data;
            } else {
                throw new Error('Failed to reset user password');
            }
        })
        .catch((error) => {
            throw error;
        });
};

// Function to get all usernames and emails.
export const getAllUsernamesAndEmails = () => {
    const url = `http://localhost:8000/auth/register`
    return axios.get(url)
        .then((response) => {
            if (response.status === 200) {
                return response.data;
            } else {
                throw new Error('Failed to fetch user details');
            }
        })
        .catch((error) => {
            throw error;
        });
};


// Function to get user details for a specific username and JWT token.
export const getUserDetails = (username, jwtToken) => {
    const url = `http://localhost:8000?action=getAllUserDetails&username=${username}&jwtToken=${jwtToken}`
    return axios.get(url)
        .then((response) => {
            if (response.status === 200) {
                return response.data;
            } else {
                throw new Error('Failed to fetch user details');
            }
        })
        .catch((error) => {
            throw error;
        });
};

// Function to get all user transactions for a specific username and JWT token.
export const getAllUserTransactions = (username, jwtToken) => {
    const url = `http://localhost:8000?action=getAllUserTransactions&username=${username}&jwtToken=${jwtToken}`
    return axios.get(url)
        .then((response) => {
            if (response.status === 200) {
                return response.data;
            } else {
                throw new Error('Failed to fetch user transactions');
            }
        })
        .catch((error) => {
            throw error;
        });
};

// Function to update a user's biography.
export const updateUserBiographyService = (username, jwtToken, biography) => {
    const url = `http://localhost:8000?username=${username}&jwtToken=${jwtToken}&biography=${biography}`
    return axios.put(url)
        .then((response) => {
            if (response.status === 200) {
                return response.data
            } else {
                throw new Error('Failed to update user biography');
            }
        }).catch((error) => {
            throw error;
        });
};


// Function to change a user's password.
export const changeUserPassword = (username, jwtToken, oldPassword, newPassword) => {
    const url = `http://localhost:8000/change-password?username=${username}&jwtToken=${jwtToken}&oldPassword=${oldPassword}&newPassword=${newPassword}`
    return axios.put(url)
        .then((response) => {
            if (response.status === 200) {
                return response.data
            } else {
                throw new Error('Failed to change User password');
            }
        }).catch((error) => {
            throw error;
        });
};

// Function to log out a user.
export const logoutUser = (username, jwtToken) => {
    const url = `http://localhost:8000/custom-logout?username=${username}&jwtToken=${jwtToken}`
    return axios.post(url)
};

// Function to send inquiry email.
export const sendInquiryEmail = (title, content, username, jwtToken) => {
    const url = `http://localhost:8000?title=${title}&content=${content}&username=${username}&jwtToken=${jwtToken}`
    return axios.post(url)
};

