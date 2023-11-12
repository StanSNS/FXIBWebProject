import '../Component/About/about.css'
import Skeleton from "react-loading-skeleton";
import React from "react";

const AboutSkeleton = () => {
    return (
        <div>
            <div className="addMarginTop">
                <section className="about_section mt-0">
                    <div className="container">
                        <div className="row">
                            <div className="col-md-6 order-md-2">
                                <div className="img-box leftCard">
                                    <span className="floatUp align-items-center">
                                        <Skeleton height={250}/>
                                    </span>
                                </div>
                            </div>
                            <div className="col-md-6 order-md-1">
                                <div className="aboutContent">
                                    <h2><Skeleton/></h2>
                                    <p><span className="customTextSizeSkeleton"> <Skeleton height={200}/></span></p>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
            </div>

            <div className="addMarginTop">
                <section className="about_section mt-5">
                    <div className="container">
                        <div className="row">
                            <div className="col-md-6">
                                <div className="img-box rightCard">
                                    <span className="floatDown align-items-center">
                                        <Skeleton height={250} />
                                    </span>
                                </div>
                            </div>
                            <div className="col-md-6">
                                <div className="aboutContent">
                                    <h2><Skeleton/></h2>
                                    <p><span className="customTextSizeSkeleton"> <Skeleton height={200}/></span></p>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
            </div>
        </div>
    )
}
export default AboutSkeleton