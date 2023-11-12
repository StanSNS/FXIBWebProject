import Skeleton from "react-loading-skeleton";
import React from "react";

const PricingSkeleton = ({repeat}) => {
    return Array(repeat).fill(0).map((_, index) => (
        <div key={index + Math.random()}>

            <div className="box">
                <div className="priceBox">
                    <h2><Skeleton width={100}/></h2>
                    <h6>
                        <span className="align-text-bottom"><Skeleton width={160} height={25}/></span>
                    </h6>
                    <p className="mb-0"><Skeleton width={190}/></p>
                    <p className="mb-0"><Skeleton width={200}/></p>
                    <p className="mb-0"><Skeleton width={220}/></p>
                    <p className="mb-0"><Skeleton width={210}/></p>
                    <p className="mb-0"><Skeleton width={190}/></p>
                    <p className="mb-0"><Skeleton width={210}/></p>
                    <p className="mb-0"><Skeleton width={220}/></p>
                    <p className="mb-0"><Skeleton width={180}/></p>
                </div>
                <div className="btn-box mt-3 mb-3">
                    <Skeleton width={180} height={35}/>
                </div>
            </div>
        </div>
    ))
}
export default PricingSkeleton