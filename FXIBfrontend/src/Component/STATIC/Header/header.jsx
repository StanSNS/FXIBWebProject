import './header.css';
import {Link, useNavigate} from 'react-router-dom';
import {Button, Modal, Nav, Navbar} from "react-bootstrap";
import React, {useEffect, useState} from "react";
import {FaCheck, FaClipboardList, FaPen, FaSignOutAlt, FaSyncAlt, FaTimes, FaUser} from "react-icons/fa";
import {GiHamburgerMenu} from "react-icons/gi";
import {
    getToken,
    isAdministrator,
    isUserBanned,
    isUserLoggedIn,
    loggedUserEmail,
    loggedUserUsername,
    logout
} from "../../../Service/AuthService";
import {
    getAllUserTransactions,
    getUserDetails,
    logoutUser,
    updateUserBiographyService
} from "../../../Service/UserService";
import {FaUserLargeSlash} from "react-icons/fa6";
import UserDetailsSkeleton from "../../../SkeletonLoader/UserDetailsSkeleton";
import UserTransactionsSkeleton from "../../../SkeletonLoader/UserTrsnsactionsSkeleton";

export default function Header() {

    const isBanned = isUserBanned(); // Check if the user is banned
    const isAuth = isUserLoggedIn(); // Check if the user is authenticated/logged in
    const isAdmin = isAdministrator(); // Check if the user is an administrator
    const navigator = useNavigate(); // Import the useNavigate hook from the routing library
    const [isUserDetailsLoading, setIsUserDetailsLoading] = useState(true); // State variable to track loading status of user details
    const [isTransactionsLoading, setIsTransactionsLoading] = useState(true); // State variable to track loading status of transactions


    const [showUserDetailsModal, setShowUserDetailsModal] = useState(false);// State to control the visibility of the user details modal
    const [userDetails, setUserDetails] = useState(null);// State to store user details data
    const [editedBiography, setEditedBiography] = useState('');// State to store the edited biography
    const [showTransactionsModal, setShowTransactionsModal] = useState(false);// State to control the visibility of the transactions modal
    const [userTransactions, setUserTransactions] = useState([]);// State to store user transactions data
    const handleResetPassword = () => setShowUserDetailsModal(false);    // Function to handle resetting the password
    const [isEditingBiography, setIsEditingBiography] = useState(false);    // State to track if the biography is being edited
    const [isEditingModal, setIsEditingModal] = useState(false);// State to track if an editing modal is open
    const paintClass = isBanned ? 'myRedColorPaint' : 'myBlueColorPaint';// Determine the CSS class for painting based on user ban status

    // Character count information for the biography
    const maxCharacterCount = 95;
    const characterCount = maxCharacterCount - editedBiography.length;
    const characterCountStyle = {color: characterCount >= 0 ? 'var(--myGreen)' : 'var(--myOrange)',};


    // Fetch user details when the user details modal is shown
    useEffect(() => {
        async function fetchUserDetails() {
            try {
                const userDetailsResponse = await getUserDetails(loggedUserUsername(), getToken().substring(7));
                setUserDetails(userDetailsResponse);
                setIsUserDetailsLoading(false)
            } catch (error) {
                console.error("Failed to fetch user details:" + error);
            }
        }

        if (showUserDetailsModal) {
            fetchUserDetails();
        }
    }, [showUserDetailsModal]);

    // Function to handle showing the user transactions
    const handleShowTransactions = async () => {
        getAllUserTransactions(loggedUserUsername(), getToken().substring(7))
            .then((data) => {
                setUserTransactions(data)
                setIsTransactionsLoading(false)
            })
        setShowTransactionsModal(true);
    };

    // Function to handle editing the biography
    const handleEditBiography = () => {
        setEditedBiography(userDetails.biography);
        setIsEditingBiography(true);
        setIsEditingModal(true);
    };

    // Function to update the user's biography
    const updateUserBiography = async () => {
        try {
            await updateUserBiographyService(loggedUserUsername(), getToken().substring(7), editedBiography);
            const userDetailsResponse = await getUserDetails(loggedUserUsername(), getToken().substring(7));
            setUserDetails(userDetailsResponse);
        } catch (error) {
            console.error("Error updating user biography:" + error);
        }
    }

    // Function to save the edited biography
    const handleSaveBiography = () => {
        setIsEditingBiography(false);
        setUserDetails({...userDetails, biography: editedBiography});
        setIsEditingModal(false);
        updateUserBiography();
    };

    // Function to handle changes in the biography input
    const handleBiographyChange = (e) => {
        const inputText = e.target.value;
        setEditedBiography(inputText);
    };

    // Function to cancel the biography editing
    const handleCancelBiography = () => {
        setIsEditingBiography(false);
        setIsEditingModal(false);
    };


    // Function to hide the user details modal
    function handleHideUserDetailsModal() {
        setShowUserDetailsModal(false);
    }

    // Function to show the user details modal
    function handleShowUserDetailsModal() {
        setShowUserDetailsModal(true);
    }

    // Function to handle user logout
    function handleLogout() {
        logoutUser(loggedUserUsername(), getToken().substring(7))
            .then((response) => {
                if (response.status === 200) {
                    setShowUserDetailsModal(false);
                    logout();
                    navigator('/')
                    window.location.reload();
                }
            }).catch((error) => {
            console.error(error)
        })
    }

    return (
        <div>
            {}
            <Navbar expand="lg" className="myNav custom_nav-container">
                <Link to="/" className="navbar-brand colorLogo">
                    <button data-text="Awesome" className="button ml-1 ">
                        <span className="actual-text">&nbsp;FXIB&nbsp;</span>
                        <span className="hover-text" aria-hidden="true">&nbsp;FXIB&nbsp;</span>
                    </button>
                </Link>
                <Navbar.Toggle aria-controls="navbarSupportedContent"> <span
                    className="hamburgerIcon"><GiHamburgerMenu/></span></Navbar.Toggle>
                <Navbar.Collapse id="navbarSupportedContent" className="text-center">
                    <Nav className="ml-auto navbar-nav">
                        {(isAdmin &&
                            <Link to="/admin" className="nav-link">
                                <span>Admin</span>
                            </Link>
                        )}
                        <Link to="/about" className="nav-link nav-item">
                            <span>About</span>
                        </Link>
                        <Link to="/pricing" className="nav-link">
                            <span>Pricing</span>
                        </Link>
                        <Link to="/accounts" className="nav-link">
                            <span>Accounts</span>
                        </Link>
                        <Link to="/partners" className="nav-link">
                            <span>Partners</span>
                        </Link>
                        <Link to="/community" className="nav-link">
                            <span>Community</span>
                        </Link>
                        {(isAuth &&
                            <Link to="" className="nav-link" onClick={() => handleShowUserDetailsModal()}>
                                {loggedUserUsername() && (
                                    <span>@{loggedUserUsername().toUpperCase()}</span>
                                )}
                            </Link>
                        )}
                    </Nav>
                </Navbar.Collapse>
            </Navbar>

            <Modal show={showUserDetailsModal} onHide={handleHideUserDetailsModal} centered
                   backdrop={isEditingModal ? "static" : true}>
                <div className="modal-content">
                    <Modal.Header className="removeBorder">
                        <Modal.Title>
                            <div>
                                {isBanned
                                    ? (<span className="align-text-bottom myRedColorPaint"><FaUserLargeSlash/></span>)
                                    : (<span className="align-text-bottom myBlueColorPaint"><FaUser/></span>
                                    )}
                                <span className={paintClass}> User Details </span>
                            </div>
                        </Modal.Title>
                        {isEditingModal ? null : (
                            <Button variant="link" className="close" onClick={() => setShowUserDetailsModal(false)}>
                                <FaTimes/>
                            </Button>
                        )}
                    </Modal.Header>
                    <Modal.Body>

                        {isUserDetailsLoading && <UserDetailsSkeleton/>}

                        {userDetails && (
                            <span>
                                <p className="mb-1">
                                    <span className={paintClass}><strong> Username: </strong> </span>
                                    {userDetails.username}
                                </p>
                                <p className="mb-1">
                                    <span className={paintClass}> <strong> Registration Date: </strong></span>
                                    {userDetails.registrationDate}
                                </p>
                                <p className="mb-1">
                                    <span className={paintClass}> <strong> Subscription Plan: </strong></span>
                                    {userDetails.subscription}
                                </p>
                                <p className="mb-3">
                                    <span className={paintClass}> <strong> Email: </strong> </span>
                                    {userDetails.email}
                                </p>
                                <div className="d-flex justify-content-between ">
                                    <span className="biographyTextSize">
                                         <span className={paintClass}>
                                             <strong>Biography: </strong>
                                         </span>

                                        {isEditingBiography ? (
                                            <div className="input-wrapperHeader">
                                                <div style={characterCountStyle}>{`(${characterCount})`}</div>
                                                <input
                                                    type="text"
                                                    className="input-boxHeader"
                                                    placeholder={`Add biography - (max. characters - ${maxCharacterCount})`}
                                                    name="password"
                                                    value={editedBiography}
                                                    onChange={handleBiographyChange}
                                                />
                                                <span className="underline ml-5"></span>
                                            </div>
                                        ) : (
                                            <div className="biographyContainer">
                                                {userDetails.biography}
                                            </div>
                                        )}
                                    </span>
                                    <span>
                                    {isEditingBiography
                                        ? (<><Button variant="info" className="ml-0 mr-1" onClick={handleSaveBiography}>
                                            <span className="align-text-top">Save </span><FaCheck/>
                                        </Button>
                                            <Button variant="info" className="ml-0 mr-0"
                                                    onClick={handleCancelBiography}>
                                                <span className="align-text-top">Cancel </span><FaTimes/>
                                            </Button></>)
                                        : (
                                            <Button variant="info"
                                                    className="changeBUttonWidth justify-content-center"
                                                    onClick={handleEditBiography}
                                                    disabled={isBanned}>
                                                <span className="align-text-top">Change </span>
                                                <span className="ml-1"> <FaPen/></span>
                                            </Button>
                                        )}
                                    </span>
                                </div>
                            </span>
                        )
                        }
                    </Modal.Body>
                    <Modal.Footer className="justify-content-center removeBorder p-0 m-0 mb-3">
                        <Button
                            className="ml-0 mr-0"
                            variant="dark"
                            onClick={handleShowTransactions}
                            disabled={isEditingModal}>
                            <span className="align-text-top">Transactions</span>
                            <span className="ml-1 align-text-top"></span> <FaClipboardList/>
                        </Button>
                        <Link to='/change-password' className=" mr-0">
                            <Button variant="primary" onClick={handleResetPassword} disabled={isEditingModal}>
                                <span className="align-text-top">Change Password</span>
                                <span className="ml-1 align-text-top"></span> <FaSyncAlt/>
                            </Button>
                        </Link>
                        <Link to='/' className=" ml-0 ">
                            <Button variant="danger" onClick={handleLogout} disabled={isEditingModal}>
                                <span className="align-text-top">Logout</span>
                                <span className="ml-1 align-text-top"></span> <FaSignOutAlt/>
                            </Button>
                        </Link>
                    </Modal.Footer>
                </div>
            </Modal>

            <Modal show={showTransactionsModal} onHide={() => setShowTransactionsModal(false)} centered size="xl">
                <div>
                    <Modal.Header className="removeBorder">
                        <Modal.Title>
                            <div className="mr-5">
                                <span className="userIcon align-text-bottom "> <FaClipboardList/> </span>
                                <span> Transactions: {userDetails && userTransactions ? userTransactions.length : 0} </span>
                            </div>
                        </Modal.Title>


                        <div className=" customMessageStyleTopModal mt-1 font-weight-bolder">These transactions were
                            made with <span className="customLinkDesign">EMAIL: </span> {loggedUserEmail()}
                        </div>


                        <Button
                            variant="link"
                            className="close"
                            onClick={() => setShowTransactionsModal(false)}>
                            <FaTimes/>
                        </Button>
                    </Modal.Header>
                    <Modal.Body>
                        <table className="table table-bordered text-center">
                            <thead>
                            <tr>
                                <th>Billing Date</th>
                                <th>Duration</th>
                                <th>End of Billing Date</th>
                                <th>Amount</th>
                                <th>Card</th>
                                <th>Status</th>
                                <th>Receipts</th>
                                <th>Description</th>
                            </tr>
                            </thead>
                            <tbody>

                            {isTransactionsLoading && <UserTransactionsSkeleton repeat={3}/>}

                            {userTransactions.map((transaction, index) => (
                                <tr key={index} className="text-center">
                                    <td>{transaction.billingDate}</td>
                                    <td>{transaction.duration}</td>
                                    <td>{transaction.endOfBillingDate}</td>
                                    <td>{transaction.amount}</td>
                                    <td>{transaction.card}</td>
                                    <td>
                                        {transaction.status === "succeeded" ? (
                                            <span style={{color: "var(--myGreen)"}}>{transaction.status}</span>
                                        ) : transaction.status === "pending" ? (
                                            <span style={{color: "yellow"}}>{transaction.status}</span>
                                        ) : (
                                            <span style={{color: "red"}}>{transaction.status}</span>
                                        )}
                                    </td>

                                    <td>
                                        <Link to={transaction.receipt} target="_blank"
                                              rel="noopener noreferrer"> Show </Link>
                                    </td>
                                    <td>{transaction.description}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </Modal.Body>
                    <Modal.Footer className="justify-content-center removeBorder p-0 m-0 mb-3">
                        <Button
                            className="ml-0 mr-0"
                            variant="dark"
                            onClick={() => setShowTransactionsModal(false)}>
                            Close
                        </Button>
                    </Modal.Footer>
                </div>
            </Modal>
        </div>
    );
}
