import React, {useEffect, useState} from "react";
import './footer.css';
import 'bootstrap/dist/css/bootstrap.min.css'
import {FaArrowTrendDown, FaArrowTrendUp, FaDiscord, FaReddit, FaTelegram, FaTwitter} from "react-icons/fa6";
import {Link} from 'react-router-dom';
import {FaRegQuestionCircle} from "react-icons/fa";
import {PiInstagramLogoFill} from "react-icons/pi";
import {IoCalendarSharp} from "react-icons/io5";
import {BiSolidBank} from "react-icons/bi";
import {GiBank} from "react-icons/gi";
import {Button, Modal} from "react-bootstrap";
import {AiFillInfoCircle} from "react-icons/ai";
import {getAllTradingAccountsForFooter} from "../../../Service/TradingAccount";

export default function Footer() {

    const [tradingAccounts, setTradingAccounts] = useState([]); // State to store trading accounts data
    const [showModal, setShowModal] = useState(false); // State to control the visibility of the modal
    const [redirectUrl, setRedirectUrl] = useState(""); // State to store the URL to be opened

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
        rootElement.scrollIntoView({ behavior: 'smooth' });
    }


    // Function to show the modal
    const handleShowModal = () => {
        setShowModal(true);
    };

    // Function to close the modal and open the stored URL if available
    const handleCloseModal = () => {
        setShowModal(false);
        if (redirectUrl) {
            window.open(redirectUrl, '_blank');
        }
    };

    // Function to handle a link click by showing the modal
    const handleLinkClick = (event) => {
        event.preventDefault();
        handleShowModal();
    };

    // Function to handle the OK button click in the modal
    const handleOKButtonClick = () => {
        handleCloseModal();
    };

    // Function to set the redirect URL
    const setRedirect = (url) => {
        setRedirectUrl(url);
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
                                        <Link to={"https://buy.stripe.com/test_dR67vr7nS0v0fewbII"}
                                              onClick={(event) => {
                                                  handleLinkClick(event);
                                                  setRedirect("https://buy.stripe.com/test_dR67vr7nS0v0fewbII");
                                              }}>
                                            <span className="align-text-bottom "><IoCalendarSharp/> </span>1 Month
                                        </Link>
                                    </li>
                                    <li className="mb-1 mt-2">
                                        <Link to={"https://buy.stripe.com/test_28o02ZbE85Pk4zScMN"}
                                              onClick={(event) => {
                                                  handleLinkClick(event);
                                                  setRedirect("https://buy.stripe.com/test_28o02ZbE85Pk4zScMN");
                                              }}>
                                            <span className="align-text-bottom "><IoCalendarSharp/> </span>6 Months
                                        </Link>
                                    </li>
                                    <li className="mt-2">
                                        <Link to={"https://buy.stripe.com/test_3csbLH4bGa5Ad6ofZ0"}
                                              onClick={(event) => {
                                                  handleLinkClick(event);
                                                  setRedirect("https://buy.stripe.com/test_3csbLH4bGa5Ad6ofZ0");
                                              }}>
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
            <Modal show={showModal} onHide={handleCloseModal} backdrop="static" keyboard={false}>
                <Modal.Header className="removeBorder">
                    <Modal.Title>
                        <span className="align-text-bottom myLittleIconInfo"><AiFillInfoCircle/></span>
                        <span> Information</span>
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body className="mt-2">
                    <p className="text-center">
                        <strong> Before proceeding, kindly ensure that you use the email address associated with your
                            registration on our website in the billing form.</strong></p>
                </Modal.Body>
                <Modal.Footer className='removeBorder'>
                    <Button variant="info" onClick={handleOKButtonClick}
                            className="mx-auto">
                        OK
                    </Button>
                </Modal.Footer>
            </Modal>
        </footer>
    );
};