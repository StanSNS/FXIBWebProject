import Skeleton from "react-loading-skeleton";
import React from "react";
import '../Component/Partners/partners.css'

const PartnerSkeleton = ({repeat}) => {
    return Array(repeat).fill(0).map((_, index) => (
        <div key={index + Math.random()}>
            <div className="partnersBox">
                <div className="mb-1 text-center">
                    <Skeleton circle width={35} height={35}/>
                </div>
                <h4 className="mb-2"><Skeleton width={380}/></h4>
                <p className="mb-0 text-center"><Skeleton width={220}/></p>
                <p className="mb-0 text-center"><Skeleton width={230}/></p>
                <p className="mb-0 text-center"><Skeleton width={200}/></p>
                <p className="mb-0 text-center"><Skeleton width={180}/></p>
                <p className="mb-0 text-center"><Skeleton width={180}/></p>
                <p className="mb-0 text-center"><Skeleton width={240}/></p>

                <p className="mb-0 text-center"><Skeleton width={200} height={40} borderRadius={30}/></p>
            </div>

            <div className="mb-5"></div>
        </div>
    ))
}
export default PartnerSkeleton