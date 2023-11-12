import './twoFactor.css';
import {FaSignInAlt} from 'react-icons/fa';
import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {loginWith2FACode, saveLoggedUser, storeToken} from "../../Service/AuthService";

export default function TwoFactorAuth() {

    const navigator = useNavigate(); // Use the useNavigate hook for navigation
    const [errorMessage, setErrorMessage] = useState('');// State to store error messages
    const [verificationCode, setVerificationCode] = useState(['', '', '', '', '', '']);// State to store the verification code as an array of individual digits

    // Function to handle changes in the verification code input
    const handleCodeChange = (index, value) => {
        const newVerificationCode = [...verificationCode];
        for (let i = 0; i < value.length && index + i < newVerificationCode.length; i++) {
            newVerificationCode[index + i] = value[i];
        }
        if (value === '') {
            newVerificationCode[index] = '';
        }
        setVerificationCode(newVerificationCode);
    };


    // Function to handle the verification process
    const handleVerify = () => {
        // Combine the verification code array into a single code string
        const code = verificationCode.join('');

        // Validate the code
        if (code.length < 6) {
            setErrorMessage('Please fill in all the boxes.');
        } else if (!/^\d+$/.test(code)) {
            setErrorMessage('Verification code must contain only digits.');
        } else {
            // Retrieve the username and password from session storage
            let username = sessionStorage.getItem("username");
            let password = sessionStorage.getItem("password");

            // Make an API call to verify the 2FA code
            loginWith2FACode(username, password, code)
                .then((response) => {
                    if (response.status === 200) {
                        // Clear session storage
                        sessionStorage.clear();

                        // Retrieve the access token and user role from the response
                        const token = 'Bearer ' + response.data.accessToken;
                        const role = response.data.role;

                        // Store the token and user role
                        storeToken(token);
                        saveLoggedUser(username, role);

                        // Navigate to the main page and reload the window
                        navigator("/");
                        window.location.reload(false);
                    }
                }).catch(error => {
                console.error(error);
            });

            // Clear any previous error messages
            setErrorMessage('');
        }
    };

    return (
        <div className="container mt-5 mt-5">
            <div className="row justify-content-center mt">
                <div className="col-md-6">
                    <div className="card changeBorderRP">
                        <div className="text-center">
                            <div className="waveRPUP waveRPUP1 text-center"></div>
                            <h4 className="mt-5">Two Factor Authentication</h4>
                            <h6 className="">Enter the 6-digit code sent to your email</h6>
                        </div>

                        <div className="customBoxes ml-5 mt-3">
                            <div className="row mb-4">
                                {verificationCode.map((value, index) => (
                                    <div className="col-lg-2 col-md-2 col-2 ps-0 ps-md-2" key={index}>
                                        <input
                                            type="text"
                                            className="form-control text-lg text-center font-weight-bolder"
                                            placeholder="_"
                                            aria-label="2fa"
                                            value={value}
                                            onChange={(e) => handleCodeChange(index, e.target.value)}
                                        />
                                    </div>
                                ))}
                            </div>
                        </div>

                        <div className="form-group text-center">
                            <button
                                type="submit"
                                className="loginButtonInput mt-4"
                                onClick={handleVerify}
                            >
                                Verify
                                <span className="ml-2 align-text-bottom"><FaSignInAlt/>{' '}</span>
                            </button>
                            {errorMessage && (
                                <p className="text-danger mt-3">
                                    <strong> {errorMessage}</strong>
                                </p>
                            )}
                            <div className="waveRPDown waveRPDown1 text-center mr-5 mt-5"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
