import React from "react";
import {Card} from "react-bootstrap";
import Skeleton from "react-loading-skeleton";

const AdminSkeleton = ({repeat}) => {
    return Array(repeat).fill(0).map((_, index) => (
        <div key={index + Math.random()}>
            <div className="col-md-3 mb-4 user-card">
                <Card className="custom-rounded ">
                    <Card.Body className="d-flex flex-column rounded">
                        <Card.Title
                            className="font-weight-bold mb-0 usernameColor"><Skeleton width={120}/>
                        </Card.Title>
                        <Card.Text className="mt-2 p-1">
                            <strong><Skeleton width={200}/></strong>
                            <br/>
                            <strong><Skeleton width={240}/></strong>
                            <br/>
                            <strong><Skeleton width={280}/></strong>
                        </Card.Text>

                        <div className="text-center">
                            <Skeleton width={160}/>
                        </div>

                    </Card.Body>
                </Card>
            </div>
        </div>
    ))

}
export default AdminSkeleton