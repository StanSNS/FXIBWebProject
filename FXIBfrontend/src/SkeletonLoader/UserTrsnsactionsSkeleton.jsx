import Skeleton from "react-loading-skeleton";
import React from "react";

const UserTransactionsSkeleton = ({repeat}) => {
    return Array(repeat).fill(0).map((_, index) => (

        <tr key={index + Math.random()} className="text-center">
            <td><Skeleton/></td>
            <td><Skeleton/></td>
            <td><Skeleton/></td>
            <td><Skeleton/></td>
            <td><Skeleton/></td>
            <td><Skeleton/></td>
            <td><Skeleton/></td>
            <td><Skeleton/></td>
        </tr>
    ))
}
export default UserTransactionsSkeleton