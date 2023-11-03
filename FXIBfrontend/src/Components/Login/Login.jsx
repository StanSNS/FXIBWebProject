import './login.css'
import {Link, useNavigate} from "react-router-dom";
import React, {useState} from "react";
import {FaQuestionCircle, FaSignInAlt, FaUser, FaUserPlus} from "react-icons/fa";
import {loginAPICall, saveLoggedUser, storeToken} from "../../Service/AuthService";

export default function Login() {

    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const navigator = useNavigate();
    const [error, setError] = useState("");

    // Handle the login form submission
    async function handleLoginForm(e) {
        e.preventDefault(); // Prevent the default form submission behavior.

        // Check if username and password are provided.
        if (!username && !password) {
            setError("Username and password are required.");
            return;
        }

        // Check if username is provided.
        if (!username) {
            setError("Username is required.");
            return;
        }

        // Check if password is provided.
        if (!password) {
            setError("Password is required.");
            return;
        }

        // Check if the username meets the length criteria.
        if (username.length < 4 || username.length > 10) {
            setError("Username must be between 4 and 10 characters.");
            return;
        }

        // Check if the password meets the length criteria.
        if (password.length < 8) {
            setError("Password must be at least 8 characters.");
            return;
        }

        try {
            // Attempt to log in by calling the loginAPICall function.
            await loginAPICall(username, password)
                .then((response) => {
                    if (response.status === 202) {
                        // If two-factor authentication is required, store the username and password and navigate to the two-factor auth page.
                        sessionStorage.setItem("username", username);
                        sessionStorage.setItem("password", password);
                        navigator("/auth/login/two-factor-auth");
                    } else if (response.status === 200) {
                        // If login is successful, clear any previous session storage, store the authentication token and user role, and navigate to the main page.
                        sessionStorage.clear();
                        const token = 'Bearer ' + response.data.accessToken;
                        const role = response.data.role;
                        storeToken(token);
                        saveLoggedUser(username, role);
                        navigator("/");
                        window.location.reload(false); // Reload the page to apply the changes.
                    }
                })
                .catch(error => {
                    console.error(error);
                    setError("Invalid username or password.");
                });
        } catch (error) {
            console.error(error);
            setError("An error occurred while processing the login.");
        }
    }


    return (
        <div className="container mt-5 mt-5">
            <div className="row justify-content-center mt">
                <div className="col-md-6 ">
                    <div className="card changeBorderLogin shadow-lg">
                        <div className="text-center">
                            <div className="waveLoginUP waveLoginUP1 text-center "></div>
                            <h4 className="mt-5">Login</h4>
                        </div>
                        <div className="ml-5">
                            <div className="form-group">
                                <div className="input-wrapper">
                                    <span className="input-icon"><FaUser/></span>
                                    <input
                                        type="text"
                                        className="input-box ml-5"
                                        placeholder="Enter your username (4-10 characters)"
                                        name="username"
                                        value={username}
                                        onChange={(e) => setUsername(e.target.value)}/>
                                    <span className="underline ml-5"></span>
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="input-wrapper">
                                    <span className="input-icon"><FaUser/></span>
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
                        </div>
                        <div className="form-group text-center">
                            <button
                                type="submit"
                                className="loginButtonInput mt-4"
                                onClick={(e) => handleLoginForm(e)}> Login
                                <span className="ml-2 align-text-bottom"><FaSignInAlt/> </span>
                            </button>
                            {error && <div className="text-center text-danger mt-2 font-weight-bolder">{error}</div>}
                            <div className="mt-4"></div>
                            <div className="form-group text-center mb-1 ">
                                <Link to='/reset-password' className="links "> <span
                                    className="align-text-bottom">  <FaQuestionCircle/></span> Forgot Password ?</Link>
                            </div>
                            <div className="form-group text-center">
                                <Link to='/auth/register' className="links"> <span
                                    className="align-text-bottom"> <FaUserPlus/> </span>Don't have an account ? Register</Link>
                            </div>
                            <div className="waveLoginDown waveLoginDown1 text-center mr-5 mt-5"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}