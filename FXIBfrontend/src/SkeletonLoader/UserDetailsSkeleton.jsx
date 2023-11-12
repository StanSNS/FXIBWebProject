import Skeleton from "react-loading-skeleton";
import React from "react";

const UserDetailsSkeleton = () => {
    return (
        <>
            <p className="mb-1"><Skeleton width={150}/></p>
            <p className="mb-1"><Skeleton width={300}/></p>
            <p className="mb-1"><Skeleton width={250}/></p>
            <p className="mb-3"><Skeleton width={210}/></p>
            <p className="mb-3"><Skeleton width={400}/></p>
        </>
    )
}
export default UserDetailsSkeleton