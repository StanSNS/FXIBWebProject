import './pricing.css';
import {Link, useNavigate} from "react-router-dom";
import {
    FaBolt,
    FaChartArea,
    FaChartLine,
    FaClipboardList,
    FaCogs,
    FaExchangeAlt,
    FaLightbulb,
    FaRegCalendarAlt,
    FaRegListAlt,
    FaRocket
} from "react-icons/fa";
import {Button, Modal} from "react-bootstrap";
import React, {useEffect, useState} from "react";
import {AiFillInfoCircle} from "react-icons/ai";
import {isUserLoggedIn, loggedUserEmail} from "../../Service/AuthService";
import {getAllPricingData} from "../../Service/InitService";
import PricingSkeleton from "../../SkeletonLoader/PricingSkeleton";

export default function Pricing() {

    const [showModal, setShowModal] = useState(false); // State to control the visibility of a modal
    const [redirectUrl, setRedirectUrl] = useState(""); // State to store a redirect URL
    const [switch1, setSwitch1] = useState(false); // State for the first switch
    const [switch2, setSwitch2] = useState(false); // State for the second switch
    const [switch3, setSwitch3] = useState(false); // State for the third switch
    const navigator = useNavigate(); // A hook for programmatic navigation
    const [pricingData, setPricingData] = useState([]); // State to store pricing data
    const [isLoading, setIsLoading] = useState(true); // State to store pricing data

    // Function to show the modal
    const handleShowModal = () => {
        setShowModal(true);
    };

    // Function to close the modal
    const handleCloseModal = () => {
        if (isUserLoggedIn()) {
            setRedirectUrl(" "); // Clear the redirect URL if the user is logged in
        }

        setShowModal(false);

        if (redirectUrl) {
            // If a redirect URL is set, open it in a new tab or window
            window.open(redirectUrl + "?prefilled_email=" + loggedUserEmail, '_blank');
        }
    };

    // Function to handle a click event on a link
    const handleLinkClick = (event) => {
        event.preventDefault();
        handleShowModal();
    };

    // Function to handle the OK button click within the modal
    const handleOKButtonClick = () => {
        // Reset all switches and close the modal
        setSwitch1(false);
        setSwitch2(false);
        setSwitch3(false);
        handleCloseModal();
    };

    // Function to set the redirect URL
    const setRedirect = (url) => {
        setRedirectUrl(url);
    };

    // Function to check if all switches are on
    const areSwitchesOn = () => {
        return switch1 && switch2 && switch3;
    };


    // Use the useEffect hook to fetch pricing data when the component mounts
    useEffect(() => {
        // Fetch pricing data using the getAllPricingData function
        getAllPricingData()
            .then((response) => {
                // Check if the response status is 200 (OK)
                if (response.status === 200) {
                    // If successful, update the state with the received data
                    setPricingData(response.data);
                    setIsLoading(false);
                }
            }).catch((error) => {
            // If there's an error, navigate to the error page and log the error
            navigator("/500");
            console.error(error);
        });
    }, [navigator]); // The empty dependency array ensures this effect runs only once when the component mounts


    return (
        <div className="sub_page">
            <section className="price_section layout_padding text-center">
                <div className="container">
                    <div className="heading_container heading_center ">
                        <div className="d-flex justify-content-center addMarginTop ">
                            <h2 className="first-letter-pricing-pop sectionHeaderStyling"><span>P</span></h2>
                            <h2 className="second-letter-pricing-pop sectionHeaderStyling"><span>r</span></h2>
                            <h2 className="third-pop-up-top sectionHeaderStyling"><span>i</span></h2>
                            <h2 className="fourth-pop-up-top sectionHeaderStyling"><span>c</span></h2>
                            <h2 className="fifth-pop-up-top sectionHeaderStyling"><span>i</span></h2>
                            <h2 className="sixth-pop-up-top sectionHeaderStyling"><span>n</span></h2>
                            <h2 className="seventh-pop-up-top sectionHeaderStyling"><span>g</span></h2>
                        </div>
                    </div>
                    <div className="price_container">

                        {isLoading && <PricingSkeleton repeat={3}/>}

                        {pricingData.map((item, index) => (
                            <div key={index} className="box">
                                <div className="priceBox">
                                    <h2>$ <span>{item.price}</span></h2>
                                    <h6>
                                        <span className="align-text-bottom">/{item.duration} Month</span>
                                        <span className="calendarIcon align-text-top"> <FaRegCalendarAlt/></span>
                                    </h6>
                                    <p className="mb-0">
                                        <span className="align-text-bottom"><FaChartLine/> </span>
                                        Capital Growth Plan
                                    </p>
                                    <p className="mb-0">
                                        <span className="align-text-bottom"><FaRegListAlt/> </span>
                                        News Trader's Blueprint
                                    </p>
                                    <p className="mb-0">
                                        <span className="align-text-bottom"><FaChartArea/> </span>
                                        Swing Trader's Masterplan
                                    </p>
                                    <p className="mb-0">
                                        <span className="align-text-bottom"><FaBolt/> </span>
                                        Scalping Success Strategy
                                    </p>
                                    <p className="mb-0">
                                        <span className="align-text-bottom"><FaCogs/> </span>
                                        Technical Titan Plan
                                    </p>
                                    <p className="mb-0">
                                        <span className="align-text-bottom"><FaLightbulb/> </span>
                                        Fundamental Focus Mission
                                    </p>
                                    <p className="mb-0">
                                        <span className="align-text-bottom"><FaExchangeAlt/> </span>
                                        Pairs Trading Powerhouse
                                    </p>
                                    <p className="mb-0">
                                        <span className="align-text-bottom"><FaClipboardList/> </span>
                                        Monthly Challenge
                                    </p>
                                </div>
                                <div className="btn-box mt-3 mb-3">
                                    <Link to={item.linkURL} target="_blank" onClick={(event) => {
                                        handleLinkClick(event);
                                        setRedirect(item.linkURL)
                                    }}>
                                        <span className="mr-1">See Details</span> <FaRocket/>
                                    </Link>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            </section>


            <Modal show={showModal} onHide={handleCloseModal} backdrop="static" keyboard={false}>
                {isUserLoggedIn()
                    ? <span>
                       <Modal.Header className="removeBorder">
                            <Modal.Title>
                                <span className="align-text-bottom myLittleIconInfo"><AiFillInfoCircle/></span>
                                <span> Information</span>
                            </Modal.Title>
                        </Modal.Header>

                       <Modal.Body className="mt-2">
                            <p className="text-center">
                                <strong> Before proceeding, kindly ensure that you use the <span
                                    className="customLinkDesign">EMAIL</span> address associated with your registration on our website in the billing form. After a successful payment, you will receive an <span
                                    className="customLinkDesign">EMAIL</span> containing further instructions.</strong>
                            </p>
                            <div className="text-center">
                                <span className="font-weight-bolder">
                                    I agree to <Link to="/risk-disclousure-terms-conditions"
                                                     className="customLinkDesign"> General Risk Disclosure</Link>
                                    <label className="switch ml-2 align-text-top">
                                        <input
                                            type="checkbox"
                                            checked={switch1}
                                            onChange={() => setSwitch1(!switch1)}
                                        />
                                        <span className="slider"></span>
                                    </label>
                                </span>
                                <br/>
                                <span className="font-weight-bolder">
                                    I agree to <Link to="/risk-disclousure-terms-conditions"
                                                     className="customLinkDesign">Terms & Conditions</Link>
                                    <label className="switch ml-2 align-text-top">
                                        <input
                                            type="checkbox"
                                            checked={switch2}
                                            onChange={() => setSwitch2(!switch2)}
                                        />
                                        <span className="slider"></span>
                                    </label>
                                </span>
                                <br/>
                                 <span className="font-weight-bolder">
                                   I will use the correct  <span className="customLinkDesign">EMAIL</span>
                                    <label className="switch ml-2 align-text-top">
                                        <input
                                            type="checkbox"
                                            checked={switch3}
                                            onChange={() => setSwitch3(!switch3)}
                                        />
                                        <span className="slider"></span>
                                    </label>
                                </span>


                            </div>
                        </Modal.Body>


                        <Modal.Footer className='removeBorder'>
                            <Button
                                variant="info"
                                onClick={handleOKButtonClick}
                                className="mx-auto"
                                disabled={!areSwitchesOn()}>
                                Continue
                            </Button>
                        </Modal.Footer>
                    </span>
                    :
                    <span>
                     <Modal.Header className="removeBorder">
                         <Modal.Title>
                             <span className="align-text-bottom myLittleIconInfoOrange"><AiFillInfoCircle/></span>
                             <span> Attention </span>
                         </Modal.Title>
                     </Modal.Header>

                     <Modal.Body className="mt-2">
                         <p className="text-center">
                             <strong> To continue, kindly
                                 <Link to="/auth/login"
                                       className="customLinkDesign"> LOGIN </Link>
                                  into your existing account or
                                 <Link
                                     to="/auth/register"
                                     className="customLinkDesign"> REGISTER </Link>
                                 a new one !</strong>
                         </p>
                     </Modal.Body>
                 </span>
                }

            </Modal>
        </div>
    )
}
