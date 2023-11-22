import React from "react";
import './hero.css';
import {SymbolOverview, TickerTape} from "react-ts-tradingview-widgets";
import {Link} from "react-router-dom";
import {isUserLoggedIn} from "../../Service/AuthService";


export default function Hero() {
    const isAuth = isUserLoggedIn(); // Check if the user is authenticated (logged in).

    return (
        <>
            <section className="hero_section d-flex">
                <div className="detail-box mr-5 ml-5">
                    <h1>
                        Your Automated Trading Partner
                        <br/>
                        Effortless Forex Trading
                    </h1>
                    <p>
                        Discover the ease of forex trading with our automated solutions, your dependable partner for
                        streamlining your trading experience in the dynamic world of foreign exchange.
                    </p>
                    {!isAuth && (
                        <div className="btn-box mt-5  ">
                            <Link to='/auth/login'>
                                <button className="loginButton ml-5 mr-5">Login</button>
                            </Link>
                            <Link to='/auth/register'>
                                <button className="registerButton ml-5">Register</button>
                            </Link>
                        </div>)}
                </div>
                <div className="tvChart">
                    <SymbolOverview colorTheme="dark"
                                    autosize
                                    chartType="line"
                                    symbols={[["DXY", "DXY"], ["DJI", "DJI"], ["NASDAQ", "NAS100"], ["USOIL", "USOIL"],
                                        ["GOLD", "GOLD"], ["EURUSD", "EURUSD"], ["USDJPY", "USDJPY"], ["GBPUSD", "GBPUSD"],
                                        ["USDCHF", "USDCHF"], ["AUDUSD", "AUDUSD"], ["USDCAD", "USDCAD"],
                                    ]}
                                    isTransparent='true' borderDownColor="#FD4646FF" wickDownColor="#FD4646FF"
                                    volumeDownColor="#FD4646FF" wickUpColor="#03A7D3FF" borderUpColor="#03A7D3FF"
                                    volumeUpColor="#03A7D3FF" dateFormat="dd MMM 'yy"/>
                </div>
            </section>
            <TickerTape colorTheme="dark" displayMode="regular"></TickerTape>
        </>
    );
};
