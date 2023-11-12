import '../Component/Accounts/accounts.css'
import React from "react";
import Skeleton from "react-loading-skeleton";

const AccountsSkeleton = ({repeat}) => {
    return Array(repeat).fill(0).map((_, index) => (
        <div key={index + Math.random()}>
            <div className="p-3 mb-4 customAccount mt-5">
                <div className="d-flex ml-3 mb-4  ">
                    <span><Skeleton width={150} height={165} borderRadius={15}/></span>

                    <div className="ml-5 mr-5 mt-4">
                        <span className="firstCustomRow">
                            <strong> <Skeleton width={260}/> </strong>
                        </span>
                        <br/>
                        <div className="pt-2">
                           <span>
                               <strong> <Skeleton width={190}/> </strong>
                           </span>
                            <br/>
                            <span>
                                <strong> <Skeleton width={170}/> </strong>
                            </span>
                            <br/>
                            <span>
                                <strong> <Skeleton width={150}/> </strong>
                            </span>
                        </div>
                    </div>
                    <div className="ml-4 mr-5 mt-4">
                          <span className="firstCustomRow ">
                             <strong> <Skeleton width={200}/> </strong>
                          </span>
                        <br/>
                        <div className="pt-2">
                            <span>
                           <strong> <Skeleton width={130}/> </strong>
                            </span>
                            <br/>
                            <span className="me-3 ">
                            <strong> <Skeleton width={110}/> </strong>
                             </span>
                            <br/>
                            <span className="me-0 ">
                              <strong> <Skeleton width={90}/> </strong>
                            </span>
                        </div>
                    </div>
                    <div className="ml-5 mt-4">
                             <span className="firstCustomRow ">
                               <strong> <Skeleton width={200}/> </strong>
                             </span>
                        <br/>
                        <div className="pt-2">
                               <span className="text-truncate me-3 ">
                                 <strong> <Skeleton width={130}/> </strong>
                               </span>
                            <br/>
                            <span className="text-truncate me-3 ">
                                  <strong> <Skeleton width={110}/> </strong>
                            </span>
                            <br/>
                            <div>
                              <span className="text-truncate me-3">
                                  <strong> <Skeleton width={90}/> </strong>
                              </span>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="mt-4 text-center">
                    <Skeleton width={100} height={35}/>
                </div>
            </div>
        </div>
    ));
}
export default AccountsSkeleton