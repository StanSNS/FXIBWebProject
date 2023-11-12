import './resetPasswordUpdate.css';
import {FaLock} from 'react-icons/fa';
import React, {useState} from 'react';
import {BsFillGearFill} from 'react-icons/bs';
import {useLocation} from "react-router-dom";
import {updateResetPasswordFromEmail} from "../../Service/UserService";
import {Button, Modal} from "react-bootstrap";
import {TbSettingsCheck} from "react-icons/tb";
export default function ResetPasswordUpdate() {

    const location = useLocation(); // A hook to access the current location
    const searchParams = new URLSearchParams(location.search); // Parse the query parameters from the URL
    const token = searchParams.get('token'); // Retrieve the token from the query parameters
    const [newPassword, setNewPassword] = useState(''); // State to store the new password input
    const [confirmPassword, setConfirmPassword] = useState(''); // State to store the password confirmation input
    const [isLoading, setIsLoading] = useState(false); // State to track loading status
    const [errorMessage, setErrorMessage] = useState(''); // State to display error messages
    const [showSuccessModal, setShowSuccessModal] = useState(false); // State to control the display of a success modal

    // Function to handle the password change
    const handlePasswordChange = async () => {
        try {
            setIsLoading(true); // Set loading status to true

            // Perform form validation and update the password
            // Call the updateResetPasswordFromEmail function with the provided token and new password
            await updateResetPasswordFromEmail(token, newPassword);

            // If successful, display the success modal
            setShowSuccessModal(true);
        } catch (error) {
            // Handle errors and set an error message
            setErrorMessage('Failed to update user biography');
        } finally {
            setIsLoading(false); // Set loading status to false
        }
    };

    // Function to handle the closure of the success modal and close the window
    const handleCloseSuccessModal = () => {
        setShowSuccessModal(false);
        window.close();
    };

    return (
        <div className="container mt-5 mt-5">
            <div className="row justify-content-center mt">
                <div className="col-md-6">
                    <div className="card changeBorderRP">
                        <div className=" text-center">
                            <div className="waveRPUP waveRPUP1 text-center "></div>
                            <h4 className="mt-5">Password Update</h4>
                        </div>
                        <div className="ml-3 mr-3">
                            <div className="form-group">
                                <div className="input-wrapper">
                                    <span className="input-icon"><FaLock/></span>
                                    <input
                                        type="password"
                                        className="input-box ml-5"
                                        placeholder="New Password"
                                        value={newPassword}
                                        onChange={(e) => setNewPassword(e.target.value)}/>
                                    <span className="underline ml-5"></span>
                                </div>
                            </div>
                        </div>
                        <div className="ml-3 mr-3">
                            <div className="form-group">
                                <div className="input-wrapper">
                                    <span className="input-icon"><FaLock/></span>
                                    <input
                                        type="password"
                                        className="input-box ml-5"
                                        placeholder="Confirm New Password"
                                        value={confirmPassword}
                                        onChange={(e) => setConfirmPassword(e.target.value)}/>
                                    <span className="underline ml-5"></span>
                                </div>
                            </div>
                        </div>
                        <div className="form-group text-center">
                            <button
                                type="button"
                                className="button submitSimpleButton mt-3"
                                onClick={handlePasswordChange}
                                disabled={isLoading}>
                                {isLoading ? 'Changing...' : 'Change'} <span className="ml-1"><BsFillGearFill/></span>
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
                        <Modal.Title> <span
                            className="customCheckIcon justify-content-center"> <TbSettingsCheck/> </span> The Password
                            has been changed ! </Modal.Title>
                    </Modal.Header>
                    <Modal.Footer className="removeBorder justify-content-center">
                        <Button variant="info" onClick={handleCloseSuccessModal}>OK</Button>
                    </Modal.Footer>
                </div>
            </Modal>
        </div>
    );
}