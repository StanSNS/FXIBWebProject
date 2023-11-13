import React, {useEffect, useState} from "react";
import './about.css';
import '../../globalCSS/css/bootstrap.css';
import {getAllAboutData} from "../../Service/InitService";
import {useNavigate} from "react-router-dom";
import rightImage from '../../images/rightImage.png';
import leftImage from '../../images/leftImage.png';
import AboutSkeleton from "../../SkeletonLoader/AboutSkeleton";

export default function About() {

    const [isLoading, setIsLoading] = useState(true);

    // Create a navigator to redirect to different pages.
    const navigator = useNavigate();

    // Initialize the state variable for storing aboutData.
    const [aboutData, setAboutData] = useState([]);

    useEffect(() => {
        // Call the API service function to fetch about data.
        getAllAboutData()
            .then((response) => {
                // Check if the API request was successful (status 200).
                if (response.status === 200) {
                    // Update the state variable with the retrieved data.
                    setAboutData(response.data);
                    setIsLoading(false)
                }
            })
            .catch((error) => {
                navigator('/500');
                console.error(error);
            });
    }, [isLoading, navigator]);
    return (
        <div className="addMarginTop">
            <div className="d-flex justify-content-center mt-3 mb-3">
                <h2 className="first-letter-about-pop sectionHeaderAboutStyling"><span>A</span></h2>
                <h2 className="second-letter-about-pop sectionHeaderAboutStyling"><span>b</span></h2>
                <h2 className="third-letter-about-pop sectionHeaderAboutStyling"><span>o</span></h2>
                <h2 className="fourth-letter-about-pop sectionHeaderAboutStyling"><span>u</span></h2>
                <h2 className="fifth-letter-about-pop sectionHeaderAboutStyling"><span>t</span></h2>
            </div>

            {isLoading && <AboutSkeleton/>}


            <div className="addMarginTop">
                {aboutData.map((currAbout, index) => (
                    <section key={index} className={`about_section mt-${index % 2 === 0 ? 5 : 0}`}>
                        <div className="container">
                            <div className="row">

                                <div className={`col-md-6 ${index % 2 === 0 ? "order-md-2" : ""}`}>
                                    <div className={`img-box ${index % 2 === 0 ? "rightCard" : "leftCard"}`}>
                                        <img src={index % 2 === 0 ? rightImage : leftImage} alt={currAbout.title}
                                             className={index % 2 === 0 ? "floatUp" : "floatDown align-items-center"}/>
                                    </div>
                                </div>

                                <div className={`col-md-6 ${index % 2 === 0 ? "order-md-1" : ""}`}>
                                    <div className="aboutContent">
                                        <h2>{currAbout.title}</h2>
                                        <p>
                                            <span className="customTextSize">{currAbout.description}</span>
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>
                ))}
            </div>
        </div>
    );
}
