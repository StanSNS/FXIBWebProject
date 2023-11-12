import {FaLock, FaShieldAlt} from "react-icons/fa";
import React, {useState} from "react";
import './changePassword.css'
import {changeUserPassword} from "../../Service/UserService";
import {getToken, loggedUserUsername} from "../../Service/AuthService";
import {useNavigate} from "react-router-dom";
import {Button, Modal} from "react-bootstrap";
import {AiOutlineCheckCircle} from "react-icons/ai";

export default function ChangePassword() {
    const navigator = useNavigate();
    const [oldPassword, setOldPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmNewPassword, setConfirmNewPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [showSuccessModal, setShowSuccessModal] = useState(false);
    const handleChangePassword = async () => {
        setErrorMessage("");
        if (!oldPassword && !newPassword && !confirmNewPassword) {
            setErrorMessage("No empty fields allowed");
            return;
        }
        if (!oldPassword) {
            setErrorMessage("Old password is required.")
            return
        }
        if (!newPassword) {
            setErrorMessage("New password is required.")
            return
        }
        if (!confirmNewPassword) {
            setErrorMessage("Confirm password is required.")
            return
        }
        if (oldPassword.length < 8) {
            setErrorMessage("Old password must be at least 8 characters.");
            return;
        }
        if (newPassword.length < 8) {
            setErrorMessage("New password must be at least 8 characters.");
            return;
        }
        if (confirmNewPassword.length < 8) {
            setErrorMessage("Confirm password must be at least 8 characters.");
            return;
        }
        if (newPassword !== confirmNewPassword) {
            setErrorMessage("New password and confirm password do not match.");
            return;
        }
        try {
            await changeUserPassword(loggedUserUsername, getToken().substring(7), oldPassword, newPassword);
            setShowSuccessModal(true);
        } catch (error) {
            setErrorMessage("Failed to change password.");
        }
    };
    const handleCloseSuccessModal = () => {
        setShowSuccessModal(false);
        navigator('/');
    };
    return (
        <div className="container mt-5 mt-5">
            <div className="row justify-content-center mt">
                <div className="col-md-6 ">
                    <div className=" changeBorderCP">
                        <div className=" text-center ">
                            <div className="waveCPUP waveCPUP1 text-center "></div>
                            <h4 className="mt-5">Change Password</h4>
                        </div>
                        <div className="ml-5">
                            <div className="form-group">
                                <div className="input-wrapper">
                                    <span className="input-icon"><FaLock/></span>
                                    <input className="input-box ml-5"
                                           type="password"
                                           placeholder="Enter your old password (min. 8 characters)"
                                           value={oldPassword}
                                           onChange={(e) => setOldPassword(e.target.value)}/>
                                    <span className="underline ml-5"></span>
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="input-wrapper">
                                    <span className="input-icon"><FaShieldAlt/></span>
                                    <input className="input-box ml-5"
                                           type="password"
                                           placeholder="Enter your password (min. 8 characters)"
                                           value={newPassword}
                                           onChange={(e) => setNewPassword(e.target.value)}/>
                                    <span className="underline ml-5"></span>
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="input-wrapper">
                                    <span className="input-icon"><FaShieldAlt/></span>
                                    <input className="input-box ml-5"
                                           type="password"
                                           placeholder="Confirm your password (min. 8 characters)"
                                           value={confirmNewPassword}
                                           onChange={(e) => setConfirmNewPassword(e.target.value)}/>
                                    <span className="underline ml-5"></span>
                                </div>
                            </div>
                        </div>
                        <div className="form-group text-center">
                            <button
                                type="button"
                                onClick={handleChangePassword}
                                className="button changePWButton mt-3">
                            </button>
                            {errorMessage && (
                                <div className="text-danger text-center font-weight-bold mt-4">{errorMessage}</div>
                            )}
                            <div className="waveCPDown waveCPDown1 text-center mr-5 mt-5"></div>
                        </div>
                    </div>
                </div>
            </div>
            <Modal show={showSuccessModal} onHide={handleCloseSuccessModal} centered>
                <div className="modal-content">
                    <Modal.Header className="removeBorder justify-content-center">
                        <Modal.Title> <span className="customCheckIcon justify-content-center"> <AiOutlineCheckCircle/> </span> Password
                            Changed Successfully </Modal.Title>
                    </Modal.Header>
                    <Modal.Footer className="removeBorder justify-content-center">
                        <Button variant="info" onClick={handleCloseSuccessModal}>OK</Button>
                    </Modal.Footer>
                </div>
            </Modal>
        </div>
    )
}