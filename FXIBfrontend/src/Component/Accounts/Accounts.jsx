import './accounts.css'
import {Link} from "react-router-dom";
import {FaChartLine, FaChartPie, FaCheck, FaHistory, FaMoneyBillWave, FaServer, FaUserTie} from "react-icons/fa";
import {BsCurrencyExchange, BsFillCalendarPlusFill, BsFillClockFill, BsFillPatchCheckFill} from "react-icons/bs";
import {RiRefreshFill} from "react-icons/ri";
import {GiReceiveMoney} from "react-icons/gi";
import React, {useEffect, useState} from "react";
import {getAllTradingAccounts} from "../../Service/TradingAccount";
import 'jquery/dist/jquery.min.js';
import 'bootstrap/dist/js/bootstrap.min.js';
import {FaArrowTrendDown, FaArrowTrendUp} from "react-icons/fa6";
import {TbClockCheck, TbClockPlus} from "react-icons/tb";
import AccountsSkeleton from "../../SkeletonLoader/AccountsSkeleton";


export default function Accounts() {
    const [isLoading, setIsLoading] = useState(true);// Initializing state variable isLoading with initial value true
    const [tradingAccounts, setTradingAccounts] = useState([]);// Initialize the state variable for storing trading accounts data.

    useEffect(() => {
        // Call the API service function to fetch trading accounts data.
        getAllTradingAccounts()
            .then((data) => {
                // Update the state variable with the retrieved data.
                setTradingAccounts(data);
                setIsLoading(false)
            })
            .catch((error) => {
                console.error('Failed to fetch trading accounts: ' + error);
            });
    }, []);


    // Function to determine an action icon based on the given action.
    const getActionIcon = (action) => {
        switch (action) {
            case 'Sell':
                return <> <strong> SELL </strong> <span className="align-text-bottom"> < FaArrowTrendDown
                    color="var(--myOrange)"/> </span>   </>;
            case 'Buy':
                return <> <strong> BUY </strong> <span className="align-text-bottom">  <FaArrowTrendUp
                    color="var(--myGreen)"/> </span>   </>;
            case 'Deposit':
                return <> <strong> DEPOSIT </strong> <span className="align-text-bottom"> < GiReceiveMoney
                    color="var(--myLighBlue)"/> </span>   </>;
            default:
                return null;
        }
    };
    return (
        <div className="container addMarginTop">
            <div className="d-flex justify-content-center mt-3 mb-3">
                <h2 className="first-letter-accounts-pop sectionHeaderAccountsStyling"><span>A</span></h2>
                <h2 className="second-letter-accounts-pop sectionHeaderAccountsStyling"><span>c</span></h2>
                <h2 className="third-letter-accounts-pop sectionHeaderAccountsStyling"><span>c</span></h2>
                <h2 className="fourth-letter-accounts-pop sectionHeaderAccountsStyling"><span>o</span></h2>
                <h2 className="fifth-letter-accounts-pop sectionHeaderAccountsStyling"><span>u</span></h2>
                <h2 className="sixth-letter-accounts-pop sectionHeaderAccountsStyling"><span>n</span></h2>
                <h2 className="seventh-letter-accounts-pop sectionHeaderAccountsStyling"><span>t</span></h2>
                <h2 className="eight-letter-accounts-pop sectionHeaderAccountsStyling"><span>s</span></h2>
            </div>

            {isLoading && <AccountsSkeleton repeat={3}/>}

            {tradingAccounts.map((account, index) => (
                <div key={account.responseIdentity} className="p-3 mb-4 customAccount mt-5">
                    <div className="row ml-3 mb-4 justify-content-center">
                        <Link
                            to={`https://www.myfxbook.com/members/FXIBulgaria/fxib-auto-trading/${account.responseIdentity}`}
                            target="_blank">
                            <img
                                className="customRounded"
                                alt="myFxBookAccountWidget"
                                src={`https://widgets.myfxbook.com/widgets/${account.responseIdentity}/small.jpg`}/>
                        </Link>
                        <div className="ml-5 mr-5 mt-4">
                        <span className="firstCustomRow">
                            <span className="align-text-bottom accIcon"> <FaUserTie/></span>
                            <strong> Account: #{account.accountIdentity} </strong>
                        </span>
                            <br/>
                            <div className="pt-2">
                           <span>
                               <span className="align-text-bottom accIcon"> <FaServer/></span>
                               <strong> Server: </strong>{account.server}
                           </span>
                                <br/>
                                <span>
                                <span className="align-text-bottom accIcon"> <GiReceiveMoney/> </span>
                                <strong> Deposit: </strong>{account.deposits}
                            </span>
                                <br/>
                                <span>
                                <span className="align-text-bottom accIcon"> <BsCurrencyExchange/></span>
                                <strong> Currency: </strong> {account.currency}
                            </span>
                            </div>
                        </div>
                        <div className="ml-4 mr-5 mt-4">
                          <span className="firstCustomRow ">
                              <span className="align-text-bottom myfxColor "> <BsFillPatchCheckFill/></span>
                              <strong> MyFxBook </strong>
                          </span>
                            <br/>
                            <div className="pt-2">
                            <span className="">
                              <span className="align-text-bottom myfxColor"> <FaMoneyBillWave/></span>
                              <strong> Balance:</strong> {account.balance}
                            </span>
                                <br/>
                                <span className="me-3 ">
                               <span className="align-text-bottom myfxColor"> <FaChartPie/> </span>
                                <strong> Equity: </strong> {account.equity}
                             </span>
                                <br/>
                                <span className="me-0 ">
                                <span className="align-text-bottom myfxColor"> <FaChartLine/></span>
                                <strong> Profit: </strong> {account.profit}
                            </span>
                            </div>
                        </div>
                        <div className="ml-5 mt-4">
                             <span className="firstCustomRow ">
                               <span className="customServer align-text-bottom updateColor"> <RiRefreshFill/></span>
                               <strong> Update </strong>
                             </span>
                            <br/>
                            <div className="pt-2">
                               <span className="text-truncate me-3 ">
                                 <span
                                     className="customCreation align-text-bottom updateColor"> <BsFillCalendarPlusFill/></span>
                                 <strong> Creation Date: </strong> {account.creationDate}
                               </span>
                                <br/>
                                <span className="text-truncate me-3 ">
                                <span className="customFirst align-text-bottom updateColor"> <BsFillClockFill/> </span>
                                <strong> First Update: </strong> {account.firstTradeDate}
                            </span>
                                <br/>
                                <div>
                              <span className="text-truncate me-3">
                                <span className="customLast align-text-bottom updateColor"> <FaHistory/> </span>
                                <strong> Last Update: </strong> {account.lastUpdateDate}
                              </span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="mt-4 text-center">
                        <button
                            data-toggle="collapse"
                            data-target={`#collapseTable${index}`}
                            className="historyButton">
                            History
                        </button>
                        <div id={`collapseTable${index}`} className={`mt-4 collapse scrollable-table`}>
                            <div className="table-container ">
                                <div className="table-wrapper">
                                    <div className="table-responsive">
                                        <table className="table text-center table-hover table-no-border mb-0 ">
                                            <thead className="thead-dark">
                                            <tr>
                                                <th>№</th>
                                                <th><span
                                                    className="align-text-bottom customColorClock"><TbClockPlus/></span> Open
                                                    Time
                                                </th>
                                                <th><span
                                                    className="align-text-bottom customColorClock"><TbClockCheck/> </span> Close
                                                    Time
                                                </th>
                                                <th>Symbol</th>
                                                <th>Action</th>
                                                <th>Pips</th>
                                                <th>Profit</th>
                                                <th>Commissions</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            {account.trades.map((item, index) => (
                                                <tr key={index}>
                                                    <td>
                                                        <span>  <strong> {account.trades.length - index} </strong></span>
                                                    </td>

                                                    <td><span>  <strong> {item.openTime} </strong></span></td>
                                                    <td><span> <strong> {item.closeTime} </strong></span></td>
                                                    <td><span><span
                                                        className="align-text-bottom ">{item.action !== 'Deposit' ?
                                                        <BsCurrencyExchange/> : ""}</span> <strong> {item.symbol}</strong></span>
                                                    </td>
                                                    <td>{getActionIcon(item.action)}</td>
                                                    <td style={{color: item.pips > 0 ? "var(--myGreen)" : item.pips < 0 ? "var(--myOrange)" : 'black'}}>
                                                        <strong> {item.pips} </strong></td>
                                                    <td style={{color: item.profit > 0 ? "var(--myGreen)" : item.profit < 0 ? "var(--myOrange)" : 'black'}}>
                                                        <strong>{item.profit}</strong></td>
                                                    <td><strong> {item.commission} </strong></td>
                                                </tr>
                                            ))}
                                            </tbody>
                                            <tfoot className="table-footer">
                                            <tr>
                                                <td colSpan="8"> Verified by Myfxbook <span
                                                    className="checkGreen align-text-bottom ml-1"> <FaCheck/> </span>
                                                </td>
                                            </tr>
                                            </tfoot>
                                        </table>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>

                </div>
            ))}
        </div>
    )
}