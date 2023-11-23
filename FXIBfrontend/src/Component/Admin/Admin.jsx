import React, {useEffect, useState} from 'react';
import {Button, Card, Form, Modal} from 'react-bootstrap';
import './admin.css';
import {FaExclamationTriangle, FaListAlt, FaLock, FaLockOpen, FaSearch, FaTimes, FaUserTimes} from "react-icons/fa";
import {getAllInquiriesForUser, getAllUsers, updateBlockedUserRoles} from "../../Service/AdminSevice";
import {getToken, loggedUserUsername} from "../../Service/AuthService";
import AdminSkeleton from "../../SkeletonLoader/AdminSkeleton";

const AdminSection = () => {

    const [isLoading, setIsLoading] = useState(true); // State variable for loading indicator
    const [users, setUsers] = useState([]); // State variable for storing an array of users
    const [inquiries, setInquiries] = useState([]); // State variable for storing an array of inquiries
    const [showConfirmationModal, setShowConfirmationModal] = useState(false); // State variable to control the visibility of a confirmation modal
    const [userToBlock, setUserToBlock] = useState(null); // State variable to store information about the user to be blocked
    const [searchTerm, setSearchTerm] = useState(''); // State variable for storing the search term used in filtering
    const [selectedUser, setSelectedUser] = useState(''); // State variable for storing the selected user
    const [showInquiriesModal, setShowInquiriesModal] = useState(false); // State variable to control the visibility of an inquiries modal

    const filteredUsers = users.filter((user) => user.username.toLowerCase().includes(searchTerm.toLowerCase()));  // Filter users based on the search term.

    // Function to handle blocking and unblocking a user.
    const handleBlockAndUnblockUser = () => {
        if (userToBlock) {
            setUsers(prevUsers => {
                return prevUsers.map(user => {
                    if (user.username === userToBlock.username) {
                        const hasBannedRole = user.roles.some(role => role.name === "BANNED");
                        const updatedRoles = hasBannedRole
                            ? user.roles.filter(role => role.name !== "BANNED")
                            : [...user.roles, {id: 3, name: "BANNED"}];

                        // Call the API service to update blocked user roles.
                        updateBlockedUserRoles(loggedUserUsername(), getToken().substring(7), user.username, updatedRoles)
                            .catch((error) => {
                                console.error("Failed to block user ", error);
                            });
                        return {...user, roles: updatedRoles};
                    }
                    return user;
                });
            });
            setShowConfirmationModal(false);
        }
    };

    // Function to clear the search term.
    const handleClearClick = () => setSearchTerm('');

    // Use the useEffect hook to fetch user data when the component mounts.
    useEffect(() => {
        getAllUsers(loggedUserUsername(), getToken().substring(7))
            .then((data) => {
                setUsers(data);
                setIsLoading(false)
            })
            .catch((error) => {
                console.error('Failed to fetch user accounts:', error);
            });
    }, []);

    // Function to open the inquiries modal
    const openInquiriesModal = (currUsername) => {
        setShowInquiriesModal(true);
        getAllInquiriesForUser(loggedUserUsername(), getToken().substring(7), currUsername)
            .then((data) => {
                setInquiries(data)
                setSelectedUser(currUsername)
            })

    };

    // Function to close the inquiries modal
    const closeInquiriesModal = () => {
        setShowInquiriesModal(false);
    };


    return (
        <div className="container-fluid d-flex flex-column align-items-center admin-hero-section mt-5">
            <div className="d-flex justify-content-center mt-5 mb-5">
                <h2 className="first-letter-admin-pop sectionHeaderAdminStyling"><span>A</span></h2>
                <h2 className="second-letter-admin-pop sectionHeaderAdminStyling"><span>d</span></h2>
                <h2 className="third-letter-admin-pop sectionHeaderAdminStyling"><span>m</span></h2>
                <h2 className="fourth-letter-admin-pop sectionHeaderAdminStyling"><span>i</span></h2>
                <h2 className="fifth-letter-admin-pop sectionHeaderAdminStyling"><span>n</span></h2>
            </div>


            <Form.Group className="mb-4">
                <div className="input-group">
                    <span className="input-group-text mt-1 mr-1"><FaSearch/></span>
                    <Form.Control
                        className="inputField"
                        type="text"
                        placeholder="Search User..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}/>
                    {searchTerm && (
                        <button className="btn btn-secondary" onClick={handleClearClick}>
                            Clear <FaTimes/>
                        </button>)}
                </div>
            </Form.Group>

            {isLoading && (
                <div className="row">
                    <AdminSkeleton repeat={4}/>
                </div>
            )}

            <div className="row">
                {filteredUsers.length === 0
                    ? (<div className="text-center errorText">
                            <div>User not found <FaUserTimes/></div>
                        </div>
                    ) : (
                        filteredUsers.map((user) => (
                            <div key={user.username} className="col-md-3 mb-4 user-card">
                                <Card key={user.username}
                                      className={`custom-rounded ${user.roles.some(role => role.name === 'BANNED')
                                          ? 'blocked-user-card'
                                          : ''}`}>
                                    <Card.Body className="d-flex flex-column rounded">
                                        <Card.Title
                                            className="font-weight-bold mb-0 usernameColor customDisplayTitle">
                                            <div className="mt-1">@{user.username.toUpperCase()}</div>

                                            <button className="customColorListAdmin"
                                                    onClick={() => openInquiriesModal(user.username)}>
                                                <span><FaListAlt/></span>
                                            </button>

                                        </Card.Title>
                                        <Card.Text className="mt-2 p-1">
                                            <strong>Email:</strong> {user.email}
                                            <br/>
                                            <strong>Subscription Plan:</strong> {user.subscription}
                                            <br/>
                                            <strong>Registration Date:</strong> {user.registrationDate}
                                        </Card.Text>

                                        <Button
                                            variant={user.roles.some(role => role.name === 'BANNED')
                                                ? 'danger'
                                                : 'info'}
                                            onClick={() => {
                                                setUserToBlock(user);
                                                setShowConfirmationModal(true);
                                            }}>
                                            {user.roles.some(role => role.name === 'BANNED')
                                                ? (<> Banned <span className="ml-2"><FaLock/></span></>)
                                                : (<> Unbanned <span className="ml-2"><FaLockOpen/></span></>)}
                                        </Button>
                                    </Card.Body>
                                </Card>
                            </div>
                        ))
                    )}
            </div>

            <div className="modal-content">
                <Modal show={showConfirmationModal} onHide={() => setShowConfirmationModal(false)} centered>
                    <div className=" custom-modal-content">
                        <Modal.Header className="removeBorder">
                            <Modal.Title>Confirmation</Modal.Title>
                            <Button variant="link" className="close"
                                    onClick={() => setShowConfirmationModal(false)}><FaTimes/></Button>
                        </Modal.Header>
                        <Modal.Body className="removeBorder text-center">
                            <strong>
                                Are you sure you want to
                                <span
                                    style={{color: userToBlock?.roles.some(role => role.name === 'BANNED') ? '#17A1B7FF' : '#DB3545FF'}}>
                                    {userToBlock?.roles.some(role => role.name === 'BANNED') ? ' unban ' : ' ban '}</span>
                                @{userToBlock?.username.toUpperCase()}?
                            </strong>
                        </Modal.Body>
                        <Modal.Footer className="justify-content-center removeBorder">
                            <Button
                                variant={userToBlock?.roles.some(role => role.name === 'BANNED') ? 'danger' : 'info'}
                                onClick={() => setShowConfirmationModal(false)}>No
                            </Button>
                            <Button
                                variant={userToBlock?.roles.some(role => role.name === 'BANNED') ? 'info' : 'danger'}
                                onClick={handleBlockAndUnblockUser}>Yes</Button>
                        </Modal.Footer>
                    </div>
                </Modal>
            </div>

            <Modal show={showInquiriesModal} onHide={closeInquiriesModal} centered size="xl">
                <Modal.Header className="removeBorder">
                    <Modal.Title>User Inquiries: <span
                        className="customTextColorAdmin"> @{selectedUser}</span></Modal.Title>
                    <Button variant="link" className="close"
                            onClick={closeInquiriesModal}><FaTimes/></Button>
                </Modal.Header>
                <Modal.Body>
                    {inquiries.length === 0
                        ? (<div>
                            <h4 className="text-center">
                                <strong>No data found </strong> <FaExclamationTriangle className="mb-1 ml-1"/>
                            </h4>
                        </div>)
                        : <table className="table">
                            <thead>
                            <tr>
                                <th>INQ-ID</th>
                                <th>Submission Date</th>
                                <th>Title</th>
                            </tr>
                            </thead>
                            <tbody>
                            {inquiries.map((inquiry, index) => (
                                <tr key={index}>
                                    <td>{inquiry.customID}</td>
                                    <td>{inquiry.date}</td>
                                    <td>{inquiry.title}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    }
                </Modal.Body>
            </Modal>
        </div>
    );
};
export default AdminSection;
