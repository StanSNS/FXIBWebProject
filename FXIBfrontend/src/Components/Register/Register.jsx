import './register.css'
import {FaEnvelope, FaLock, FaSignInAlt, FaUser, FaUserPlus} from "react-icons/fa";
import {Link, useNavigate} from "react-router-dom";
import React, {useEffect, useState} from "react";
import {registerAPICall} from "../../Service/AuthService";
import {getAllUsernamesAndEmails} from "../../Service/UserService";

export default function Register() {

    const [username, setUsername] = useState(''); // State for storing the username input
    const [email, setEmail] = useState(''); // State for storing the email input
    const [password, setPassword] = useState(''); // State for storing the password input
    const [confirmPassword, setConfirmPassword] = useState(''); // State for storing the password confirmation input
    const navigator = useNavigate(); // A hook for programmatic navigation
    const [usernamesAndEmails, setUsernamesAndEmails] = useState([]); // State for storing existing usernames and emails
    const [isUsernameTaken, setIsUsernameTaken] = useState(false); // State to track whether the entered username is already taken
    const [isEmailTaken, setIsEmailTaken] = useState(false); // State to track whether the entered email is already registered
    const [error, setError] = useState(""); // State for displaying error messages

    // Use the useEffect hook to fetch existing usernames and emails when the component mounts
    useEffect(() => {
        // Fetch usernames and emails using the getAllUsernamesAndEmails function
        getAllUsernamesAndEmails()
            .then((data) => {
                // Store the fetched data in the state
                setUsernamesAndEmails(data);
            })
            .catch((error) => {
                // Log an error if there's a problem fetching user data
                console.error("Error fetching user data: ", error);
            });
    }, []);

    // Function to handle changes in the username input
    function handleUsernameChange(value) {
        setUsername(value);

        // Check if the entered username is already taken
        const taken = usernamesAndEmails.some(user => user.username === value);
        setIsUsernameTaken(taken);
    }

    // Function to handle changes in the email input
    function handleEmailChange(value) {
        setEmail(value);

        // Check if the entered email is already registered
        const taken = usernamesAndEmails.some(user => user.email === value);
        setIsEmailTaken(taken);
    }

    // Function to handle the user registration form submission
    function handleRegistrationForm(e) {
        e.preventDefault();

        // Perform form validation and handle registration
        if (!username || !password || !email || !confirmPassword) {
            setError("Fields must not be empty");
            return;
        }
        if (username.length < 4 || username.length > 10) {
            setError("Username must be between 4 and 10 characters.");
            return;
        }
        if (password.length < 8) {
            setError("Password must be at least 8 characters.");
            return;
        }
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailPattern.test(email) || email.length < 5 || email.length > 36) {
            setError("Enter a valid email address (5-36 characters).");
            return;
        }
        if (password !== confirmPassword) {
            setError("Password and confirm password do not match.");
            return;
        }
        if (isUsernameTaken) {
            alert("Username is already taken. Please choose another.");
            return;
        }
        if (isEmailTaken) {
            alert("Email is already registered. Please use a different email.");
            return;
        }

        // If all validations pass, create a registration object and call the registerAPICall function
        const register = { username, email, password };
        registerAPICall(register)
            .then(() => {
                navigator("/auth/login"); // Navigate to the login page after successful registration
            })
            .catch((error) => {
                console.error(error);
            });
    }


    return (
        <div className="container mt-5 mt-5">
            <div className="row justify-content-center mt ">
                <div className="col-md-6 ">
                    <div className="card changeBorderRegister shadow-lg">
                        <div className=" text-center">
                            <div className="waveRegisterUP waveRegisterUP1 text-center "></div>
                            <h4 className="mt-5">Register</h4>
                        </div>
                        <div className="ml-5">
                            <div className="form-group">
                                <div className="input-wrapper">
                                    <span className="input-icon"><FaUser/></span>
                                    <input
                                        type="text"
                                        placeholder="Enter your username (4-10 characters)"
                                        className="input-box ml-5"
                                        name="username"
                                        value={username}
                                        onChange={(e) => handleUsernameChange(e.target.value)}/>
                                    <span className="underline ml-5"></span>
                                </div>
                                {isUsernameTaken &&
                                    <div className="text-center text-danger mt-1 font-weight-bolder">Username is already
                                        taken.</div>}
                            </div>
                            <div className="form-group">
                                <div className="input-wrapper">
                                    <span className="input-icon"><FaEnvelope/></span>
                                    <input
                                        type="text"
                                        placeholder="Email (5-36 characters, valid format)"
                                        className="input-box ml-5"
                                        name="email"
                                        value={email}
                                        onChange={(e) => handleEmailChange(e.target.value)}
                                    />
                                    <span className="underline ml-5"></span>
                                </div>
                                {isEmailTaken &&
                                    <div className="text-center text-danger mt-1 font-weight-bolder">Email is already
                                        registered.</div>}
                            </div>
                            <div className="form-group">
                                <div className="input-wrapper ">
                                    <span className="input-icon"><FaLock/></span>
                                    <input
                                        type="password"
                                        className="input-box ml-5"
                                        placeholder="Enter your password (min. 8 characters)"
                                        name="password"
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}/>
                                    <span className="underline ml-5"></span>
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="input-wrapper ">
                                    <span className="input-icon"><FaLock/></span>
                                    <input
                                        type="password"
                                        className="input-box ml-5"
                                        placeholder="Confirm your password (min. 8 characters)"
                                        name="confirmPassword"
                                        value={confirmPassword}
                                        onChange={(e) => setConfirmPassword(e.target.value)}/>
                                    <span className="underline ml-5"></span>
                                </div>
                            </div>
                        </div>
                        <div className="form-group text-center">
                            <button
                                type="submit"
                                onClick={(e) => handleRegistrationForm(e)}
                                className="registerButtonInput mt-4"
                                disabled={isUsernameTaken || isEmailTaken}> Register <span
                                className="ml-1"><FaSignInAlt/> </span>
                            </button>
                            {error && <div className="text-center text-danger mt-3 font-weight-bolder">{error}</div>}
                            <div className="form-group text-center mt-3 links">
                                <Link to='/auth/login' className="links"><FaUserPlus/> Already have an account?
                                    Login</Link>
                            </div>
                            <div className="waveRegisterDown waveRegisterDown1 text-center mr-5 mt-4"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}
