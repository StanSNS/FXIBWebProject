import './resetPassword.css';
import {FaUser} from 'react-icons/fa';
import React, {useState} from 'react';
import {BsFillGearFill} from 'react-icons/bs';
import {userResetPassword} from "../../Service/UserService";
import {Button, Modal} from "react-bootstrap";
import {useNavigate} from "react-router-dom";
import {FaEnvelopeCircleCheck} from "react-icons/fa6";
export default function ResetPassword() {

    const navigator = useNavigate(); // A hook for programmatic navigation
    const [email, setEmail] = useState(''); // State for storing the user's email input
    const [errorMessage, setErrorMessage] = useState(''); // State for displaying error messages
    const [showSuccessModal, setShowSuccessModal] = useState(false); // State to control the display of a success modal

    // Function to handle changes in the email input
    const handleEmailChange = (e) => {
        setEmail(e.target.value);
    };

    // Function to handle the closure of the success modal and navigate to the login page
    const handleCloseSuccessModal = () => {
        setShowSuccessModal(false);
        navigator('/auth/login');
    };

    // Function to initiate the password reset process
    const handleResetPassword = () => {
        // Perform form validation and initiate the password reset
        if (!email) {
            setErrorMessage("Email is required.");
            return;
        }

        // Define a regular expression pattern to validate email format
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (!emailPattern.test(email) || email.length < 5 || email.length > 36) {
            setErrorMessage("Enter a valid email address (5-36 characters).");
            return;
        }

        // Call the userResetPassword function to request a password reset
        userResetPassword(email)
            .then(() => {
                // Display the success modal after a successful password reset request
                setShowSuccessModal(true);
            })
            .catch((error) => {
                // Handle errors and display an error message
                setErrorMessage('Failed to reset user password');
                console.error(error);
            });
    };


    return (
        <div className="container mt-5 mt-5">
            <div className="row justify-content-center mt">
                <div className="col-md-6">
                    <div className="card changeBorderRP">
                        <div className=" text-center">
                            <div className="waveRPUP waveRPUP1 text-center "></div>
                            <h4 className="mt-5">Reset Password</h4>
                        </div>
                        <div className="ml-5">
                            <div className="form-group">
                                <div className="input-wrapper">
                                    <span className="input-icon"><FaUser/></span>
                                    <input
                                        type="text"
                                        className="input-box ml-5"
                                        placeholder="Email (5-36 characters, valid format)"
                                        name="email"
                                        value={email}
                                        onChange={handleEmailChange}/>
                                    <span className="underline ml-5"></span>
                                </div>
                            </div>
                        </div>
                        <div className="form-group text-center">
                            <button
                                type="submit"
                                className="sendButton mt-3" id="sendButton"
                                onClick={handleResetPassword}>
                                <span className="msg" id="msg"></span>
                                Send <span className="ml-1 align-text-bottom"><BsFillGearFill/></span>
                            </button>
                            {errorMessage && <p className="text-danger mt-3"><strong> {errorMessage}</strong></p>}
                            <div className="waveRPDown waveRPDown1 text-center mr-5 mt-5"></div>
                        </div>
                    </div>
                </div>
            </div>
            <Modal show={showSuccessModal} onHide={handleCloseSuccessModal} centered>
                <div className="modal-content">
                    <Modal.Header className="removeBorder justify-content-center">
                        <Modal.Title> <span className="customCheckIcon justify-content-center"> <FaEnvelopeCircleCheck/> </span> A confirmation email was sent! </Modal.Title>
                    </Modal.Header>
                    <Modal.Footer className="removeBorder justify-content-center">
                        <Button variant="info" onClick={handleCloseSuccessModal}>OK</Button>
                    </Modal.Footer>
                </div>
            </Modal>
        </div>
    );
}
