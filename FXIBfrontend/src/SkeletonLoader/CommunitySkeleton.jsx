import React from "react";
import Skeleton from "react-loading-skeleton";

const CommunitySkeleton = ({repeat}) => {
    return Array(repeat).fill(0).map((_, index) => (
        <div key={index + Math.random()}>
            <div className="question">

                <div className="d-flex justify-content-between">
                    <h5 className="question-content"><Skeleton width={1200}/></h5>
                    <div className="d-flex">
                        <span className="customMessageStyle"> <Skeleton width={80}/></span>
                        <span className="ml-4 mr-1"><Skeleton circle width={45} height={45}/></span>
                    </div>
                </div>

                <p className="mb-0">
                    <span> <Skeleton width={360}/> </span>
                    <span> <Skeleton width={260}/> </span>
                </p>

                <div className="new-answer mt-0 d-flex">
                    <span className="ml-2 mt-3"> <Skeleton width={940}/></span>
                    <span className="questionNumberStyling ml-3 mt-1"><Skeleton width={60}/></span>
                    <span className="questionNumberStyling ml-3 mt-1"><Skeleton width={100} height={30} borderRadius={20}/></span>
                    <span className="questionNumberStyling ml-3 mt-1"><Skeleton width={100} height={30} borderRadius={20}/></span>
                    <span className="questionNumberStyling ml-3 mt-1"><Skeleton width={120}/></span>
                </div>
            </div>
        </div>
    ))
}
export default CommunitySkeleton