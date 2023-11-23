import React, {useEffect, useState} from "react";
import './footer.css';
import 'bootstrap/dist/css/bootstrap.min.css'
import {
    FaArrowTrendDown,
    FaArrowTrendUp,
    FaDiscord,
    FaEnvelopeCircleCheck,
    FaReddit,
    FaTelegram,
    FaTwitter
} from "react-icons/fa6";
import {Link} from 'react-router-dom';
import {FaEnvelopeOpenText, FaRegQuestionCircle, FaTimes} from "react-icons/fa";
import {PiInstagramLogoFill} from "react-icons/pi";
import {IoCalendarSharp} from "react-icons/io5";
import {BiSolidBank} from "react-icons/bi";
import {GiBank} from "react-icons/gi";
import {Button, Form, Modal} from "react-bootstrap";
import {getAllTradingAccountsForFooter} from "../../../Service/TradingAccount";
import {getToken, isUserLoggedIn, loggedUserEmail, loggedUserUsername} from "../../../Service/AuthService";
import {sendInquiryEmail} from "../../../Service/UserService";

export default function Footer() {

    const [tradingAccounts, setTradingAccounts] = useState([]); // State to store trading accounts data
    const [contactInfoModal, setContactInfoModal] = useState(false); // State to control the visibility of the contact info modal
    const [messageTitle, setMessageTitle] = useState(""); // State to store the title of a message
    const [messageContent, setMessageContent] = useState(""); // State to store the content of a message
    const [titleCharCount, setTitleCharCount] = useState(0); // State to track the character count of the message title
    const [contentCharCount, setContentCharCount] = useState(0); // State to track the character count of the message content
    const [isSubmitDisabled, setIsSubmitDisabled] = useState(true); // State to control the submit button's disabled state based on input validity
    const [titleCharColor, setTitleCharColor] = useState("text-info"); // State to determine the color of the character count for the message title
    const [contentCharColor, setContentCharColor] = useState("text-info"); // State to determine the color of the character count for the message content
    const [showSuccessModal, setShowSuccessModal] = useState(false); // State to control the visibility of the success modal


    // Fetch trading accounts data when the component mounts
    useEffect(() => {
        getAllTradingAccountsForFooter()
            .then((data) => {
                setTradingAccounts(data);
            })
            .catch((error) => {
                console.error('Failed to fetch trading accounts' + error);
            });
    }, []);

    // Function to scroll to the top of the page smoothly
    const handleScrollToTop = () => {
        const rootElement = document.documentElement;
        rootElement.scrollIntoView({behavior: 'smooth'});
    }

    // Function to handle opening the contact info modal
    const handleContactInfoModal = () => {
        setContactInfoModal(true);
    };

    // Function to handle closing the contact info modal
    const handleCloseContactInfoModal = () => {
        setContactInfoModal(false);
    };

    // Function to show success modal and automatically close it after 5 seconds
    const showSuccessAndCloseModal = () => {
        setShowSuccessModal(true);

        // Automatically close the success modal after 5 seconds
        setTimeout(() => {
            setShowSuccessModal(false);
            handleCloseContactInfoModal();
        }, 750);
    };

    // Function to handle submitting the contact info form
    const handleContactInfoSubmit = () => {
        if (messageTitle.length <= 50 && messageContent.length <= 1500) {
            sendInquiryEmail(messageTitle, messageContent, loggedUserUsername(), getToken().substring(7))
                .then((response) => {
                    if (response.status === 200) {
                        setMessageTitle("")
                        setMessageContent("")
                        setTitleCharCount(0)
                        setContentCharCount(0)
                        showSuccessAndCloseModal();
                    }
                })
                .catch((error) => {
                    // Handle error if the submission fails
                    console.error('Failed to submit contact info', error);
                });
        }
    };

    // Handle changes in the message title input
    const handleTitleChange = (e) => {
        const value = e.target.value;
        setMessageTitle(value);
        setTitleCharCount(value.length);
        updateSubmitButton(value, messageContent);
        updateCharColor(value.length, 50, setTitleCharColor);
    };

    // Handle changes in the message content input
    const handleContentChange = (e) => {
        const value = e.target.value;
        setMessageContent(value);
        setContentCharCount(value.length);
        updateSubmitButton(messageTitle, value);
        updateCharColor(value.length, 1500, setContentCharColor);
    };

    // Update the submit button's disabled state based on title and content validity
    const updateSubmitButton = (title, content) => {
        const isTitleValid = title.length <= 50 && title.length > 0;
        const isContentValid = content.length <= 1500 && content.length > 0
        setIsSubmitDisabled(!isTitleValid || !isContentValid);
    };

    // Update the character count color based on the current count and limit
    const updateCharColor = (count, limit, setColor) => {
        if (count > limit) {
            setColor("text-danger");
        } else {
            setColor("text-info");
        }
    };


    return (
        <footer className="my-footer">
            <div className="wave wave1 text-center "></div>

            <div className="footer">
                <div className="container">
                    <div className="row border_bo1 ">
                        <div className="col-md-4">
                            <br/>
                            <br/>
                            <Link to='/' className="firstCol ml-5" onClick={handleScrollToTop}>
                                <img src={require('../../../images/logoFooter.png')} alt="logo"/>
                            </Link>
                            <br/>
                            <Link to='https://stripe.com/en-bg' target="_blank" className="firstCol ml-5">
                                <img src={require('../../../images/stripe.png')} alt="stripe"/>
                            </Link>
                            <br/>
                            <Link to='/risk-disclousure-terms-conditions' className="firstCol ml-5"
                                  onClick={handleScrollToTop}>
                                <p className="font-weight-bold ml-1">
                                    General Risk Disclosure <br/> Terms &
                                    Conditions <span> <FaRegQuestionCircle/></span>
                                </p>
                            </Link>
                        </div>

                        <div className="col-lg-2 col-md-4 col-sm-6">
                            <div className="infoma">
                                <h3><a href="/partners">Partners</a></h3>
                                <ul>
                                    <li className="mb-1 mt-2">
                                        <Link to='https://admiralmarkets.com/' target='_blank'>
                                            <span className="align-text-bottom "> <BiSolidBank/></span> Admiral Markets
                                        </Link>
                                    </li>
                                    <li className="mb-1 mt-2">
                                        <Link to='https://www.icmarkets.com/global/en/' target='_blank'>
                                            <span className="align-text-bottom "><BiSolidBank/> </span>ICMarkets
                                        </Link>
                                    </li>
                                    <li className="mb-1 mt-2">
                                        <Link to='https://global.bdswiss.com/' target='_blank'>
                                            <span className="align-text-bottom "> <BiSolidBank/></span> BDSwiss
                                        </Link>
                                    </li>
                                    <li className="mb-1 mt-2">
                                        <Link to='https://ftmo.com/en/' target='_blank'>
                                            <span className="align-text-bottom "><GiBank/> </span>FTMO
                                        </Link>
                                    </li>
                                    <li className="mb-1 mt-2">
                                        <Link to='https://www.myforexfunds.com/' target='_blank'>
                                            <span className="align-text-bottom "><GiBank/> </span>MyForexFunds
                                        </Link>
                                    </li>
                                    <li className=" mt-2">
                                        <Link to='https://app.trueforexfunds.com/home/dashboard' target='_blank'>
                                            <span className="align-text-bottom "><GiBank/> </span>TrueForexFunds
                                        </Link>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <div className="col-lg-2 col-md-4 col-sm-6 ">
                            <div className="infoma">
                                <h3><a href="/pricing">Pricing</a></h3>
                                <ul>
                                    <li className="mb-1 mt-2">
                                        <Link to={"/pricing"} onClick={handleScrollToTop}>
                                            <span className="align-text-bottom "><IoCalendarSharp/> </span>1 Month
                                        </Link>
                                    </li>
                                    <li className="mb-1 mt-2">
                                        <Link to={"/pricing"} onClick={handleScrollToTop}>
                                            <span className="align-text-bottom "><IoCalendarSharp/> </span>6 Months
                                        </Link>
                                    </li>
                                    <li className="mt-2">
                                        <Link to={"/pricing"} onClick={handleScrollToTop}>
                                            <span className="align-text-bottom "><IoCalendarSharp/> </span>12 Months
                                        </Link>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <div className="col-lg-2 col-md-4 col-sm-6">
                            <div className="infoma">
                                <h3><a href="/accounts">Accounts</a></h3>
                                <ul>
                                    {tradingAccounts.map(account => (
                                        <li key={account.id} className="mb-1 mt-2">
                                            Profit: {account.gain >= 0
                                            ? `+${account.gain.toFixed(2)} % `
                                            : `-${Math.abs(account.gain).toFixed(2)} % `}
                                            {account.gain >= 0
                                                ? <FaArrowTrendUp style={{color: 'var(--myLighBlue)'}}/>
                                                : <FaArrowTrendDown style={{color: 'var(--myOrange)'}}/>}
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        </div>

                        <div className="col-lg-2 col-md-4 col-sm-6">
                            <div className="infoma">
                                <h3>Contacts</h3>
                                <ul className="conta">
                                    <li>
                                        <Link to="https://t.me/ForexIndicesBulgaria" target="_blank" className="ml-0">
                                            <FaTelegram/> <span className="ml-2">Telegram</span>
                                        </Link>
                                    </li>
                                    <li>
                                        <Link to="https://discord.gg/q4DhpvJBfp" target="_blank" className="ml-0">
                                            <FaDiscord/> <span className="ml-2">Discord</span>
                                        </Link>
                                    </li>
                                    <li>
                                        <Link to="https://www.reddit.com/user/FXIBGroup" target="_blank"
                                              className="ml-0">
                                            <FaReddit/> <span className="ml-2">Reddit</span>
                                        </Link>
                                    </li>
                                    <li>
                                        <Link to="https://twitter.com/FXIB_Group" target="_blank" className="ml-0">
                                            <FaTwitter/> <span className="ml-2">Twitter</span>
                                        </Link>
                                    </li>
                                    <li>
                                        <Link to="https://twitter.com/https://www.instagram.com/fxibgroup/"
                                              target="_blank" className="ml-0">
                                            <PiInstagramLogoFill/> <span className="ml-2">Instagram</span>
                                        </Link>
                                    </li>


                                    {isUserLoggedIn() && <li>
                                        <button className="footerInquiryButton"
                                                onClick={handleContactInfoModal}>
                                            <FaEnvelopeOpenText/> Email us !
                                        </button>
                                    </li>}

                                </ul>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="copyright">
                    <div className="container">
                        <div className="row">
                            <div className="col-md-12">
                                <p>
                                    <br/>
                                    Forex Trading Disclaimer:
                                    <br/>
                                    The content provided on this forex trading forum is for general informational
                                    purposes only
                                    and does not constitute financial advice. Forex trading involves substantial
                                    risk, and past
                                    performance is not indicative of future results. Before trading, consider your
                                    financial
                                    situation and seek advice from a qualified financial advisor. The forum is not
                                    responsible
                                    for any losses incurred from trading decisions based on the information
                                    provided. Use this
                                    forum at your own risk and comply with local regulations. Links to external
                                    sites are not
                                    endorsements. The forum's content and disclaimer may change without notice.
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <Modal show={contactInfoModal} onHide={handleCloseContactInfoModal} size="lg">
                <Modal.Header>
                    <Modal.Title>
                        <span className="align-text-bottom myLittleIconInfo"><FaEnvelopeOpenText/> </span>
                        Contact Information
                    </Modal.Title>

                    <Button
                        variant="link"
                        className="close"
                        onClick={handleCloseContactInfoModal}>
                        <FaTimes/>
                    </Button>
                </Modal.Header>

                <Modal.Body>
                    <Form>
                        <div className="row">
                            <div className="col-md-6">
                                <Form.Group controlId="formTitle">
                                    <Form.Label>Title</Form.Label>
                                    <Form.Control
                                        type="text"
                                        placeholder="Enter title"
                                        value={messageTitle}
                                        onChange={handleTitleChange}
                                    />
                                    <small className="text-muted"> <span
                                        className={`${titleCharColor}`}>{titleCharCount}</span> /50 characters </small>
                                </Form.Group>
                            </div>
                            <div className="col-md-6 ">
                                <Form.Group controlId="formContent">
                                    <Form.Label>Email: </Form.Label>
                                    <Form.Control
                                        type="text"
                                        disabled={true}
                                        value={loggedUserEmail()}
                                    />
                                </Form.Group>
                            </div>
                        </div>
                        <Form.Group controlId="formContent" className="mt-3">
                            <Form.Label>Content</Form.Label>
                            <Form.Control
                                as="textarea"
                                rows={10}
                                placeholder="Enter content"
                                value={messageContent}
                                onChange={handleContentChange}
                            />
                            <small className="text-muted"> <span
                                className={`${contentCharColor}`}>{contentCharCount}</span> /1500 characters </small>
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer className="justify-content-center">
                    <Button variant="dark" onClick={handleCloseContactInfoModal}>
                        Close
                    </Button>
                    <Button variant="info" onClick={handleContactInfoSubmit} disabled={isSubmitDisabled}>
                        Submit
                    </Button>
                </Modal.Footer>
            </Modal>


            <Modal show={showSuccessModal} onHide={() => setShowSuccessModal(false)} centered>
                <Modal.Body>
                    <h2 className="text-success text-center">Submission successful!
                        <span className="align-text-top"><FaEnvelopeCircleCheck/></span>
                    </h2>
                </Modal.Body>
            </Modal>
        </footer>
    );
};