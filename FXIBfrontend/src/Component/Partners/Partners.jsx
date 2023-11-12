import './partners.css';
import {Link, useNavigate} from 'react-router-dom';
import React, {useEffect, useState} from 'react';
import {getAllPartnersData} from '../../Service/InitService';
import {FaBalanceScaleLeft, FaClock, FaDesktop, FaDollarSign, FaExchangeAlt, FaHandshake} from "react-icons/fa";
import PartnerSkeleton from "../../SkeletonLoader/PartnerSkeleton";

export default function Partners() {

    const navigator = useNavigate(); // A hook for programmatic navigation
    const [partnersData, setPartnersData] = useState([]); // State to store partner data
    const [isLoading, setIsLoading] = useState(true); // State to store partner data

    // Use the useEffect hook to fetch partner data when the component mounts
    useEffect(() => {
        // Fetch partner data using the getAllPartnersData function
        getAllPartnersData()
            .then((response) => {
                // Check if the response status is 200 (OK)
                if (response.status === 200) {
                    // If successful, update the state with the received data
                    setPartnersData(response.data);
                    setIsLoading(false)
                }
            })
            .catch((error) => {
                // If there's an error, navigate to the error page and log the error
                navigator('/500');
                console.error(error);
            });
    }, [navigator]); // The empty dependency array ensures this effect runs only once when the component mounts


    return (
        <div className="addMarginTop">
            <section className="wholePartnersSection">
                <div className="container">
                    <div className="heading_container heading_center">
                        <div className="d-flex justify-content-center mt-3 mb-3">
                            <h2 className="first-letter-pricing-pop sectionHeaderStyling"><span>P</span></h2>
                            <h2 className="second-letter-pricing-pop sectionHeaderStyling"><span>a</span></h2>
                            <h2 className="third-pop-up-top sectionHeaderStyling"><span>r</span></h2>
                            <h2 className="fourth-pop-up-top sectionHeaderStyling"><span>t</span></h2>
                            <h2 className="fifth-pop-up-top sectionHeaderStyling"><span>n</span></h2>
                            <h2 className="sixth-pop-up-top sectionHeaderStyling"><span>e</span></h2>
                            <h2 className="seventh-pop-up-top sectionHeaderStyling"><span>r</span></h2>
                            <h2 className="eight-pop-up-top sectionHeaderStyling"><span>s</span></h2>
                        </div>
                    </div>
                </div>
                <div className="container">
                    <div className="row">

                        {isLoading && <PartnerSkeleton repeat={6}/>}

                        {partnersData.map((partner, index) => (
                            <div className={`col-md-6 col-lg-4 ${index < 3 ? 'less-rows' : ''}`} key={index}>
                                <div className="box">
                                    <div className="img-box mb-1">
                                        <img src={require(`./images/${partner.title}.jpg`)} alt={partner.title}/>
                                    </div>
                                    <div className="partnersBox">
                                        <h4 className="mb-2">{partner.title}</h4>
                                        <p className="mb-0"><span
                                            className="align-text-bottom"><FaDollarSign/>{' '}</span>{partner.firstLine}
                                        </p>
                                        <p className="mb-0"><span
                                            className="align-text-bottom"><FaExchangeAlt/>{' '}</span>{partner.secondLine}
                                        </p>
                                        <p className="mb-0"><span
                                            className="align-text-bottom"><FaBalanceScaleLeft/>{' '}</span>{partner.thirdLine}
                                        </p>
                                        <p className="mb-0"><span
                                            className="align-text-bottom"><FaHandshake/>{' '}</span>{partner.fourthLine}
                                        </p>
                                        <p className="mb-0"><span
                                            className="align-text-bottom"><FaDesktop/>{' '}</span>{partner.fifthLine}
                                        </p>
                                        <p className="mb-0"><span
                                            className="align-text-bottom"><FaDesktop/>{' '}</span>{partner.sixthLine}
                                        </p>
                                        {index < 3 && (
                                            <div className="mt-3"></div>
                                        )}
                                        {index >= 3 && (
                                            <p className=""><span
                                                className="align-text-bottom"><FaClock/>{' '}</span>{partner.seventhLine}
                                            </p>
                                        )}
                                        <Link to={partner.linkURL} target="_blank">
                                            <button className="learn-more partnerLearnMoreButton">
                                                <span aria-hidden="true" className="partnerCircle">
                                                    <span
                                                        className="partnerIcon partnerArrow">
                                                    </span>
                                                </span>
                                                <span
                                                    className="button-text">Learn More
                                                </span>
                                            </button>
                                        </Link>
                                    </div>
                                    <div className={`myWave myWave1 text-center mt-4`}></div>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            </section>
        </div>
    );
}
