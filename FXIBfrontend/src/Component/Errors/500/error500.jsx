import './error500.css'
import React from "react";

export default function Error500() {
    return (
        <div className="error-container mt-5">
            <div className="error-content">
                <div>
                    <h1 className="text-danger">Error 500</h1>
                    <p>Oops! Something went wrong on our end.</p>
                    <p>Please try again later.</p>
                </div>
            </div>
        </div>
    );
}