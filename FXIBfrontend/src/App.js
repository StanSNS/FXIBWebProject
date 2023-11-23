import Hero from "./Component/Hero/hero";
import About from "./Component/About/about";
import Footer from "./Component/STATIC/Footer/footer";
import Header from "./Component/STATIC/Header/header";
import {BrowserRouter, Navigate, Route, Routes} from 'react-router-dom'

import './globalCSS/css//bootstrap.min.css'
import Pricing from "./Component/Pricing/pricing";
import Partners from "./Component/Partners/Partners";
import Accounts from "./Component/Accounts/Accounts";
import Login from "./Component/Login/Login";
import Register from "./Component/Register/Register";
import Admin from "./Component/Admin/Admin";
import RiskAndTerms from "./Component/RiskAndTerms/RiskAndTerms";
import Community from "./Component/Community/community";
import ChangePassword from "./Component/ChangePassword/changePassword";
import ResetPassword from "./Component/ResetPassword/resetPassword";
import Error500 from "./Component/Errors/500/error500";
import Error404 from "./Component/Errors/404/error404";
import {isAdministrator, isUserLoggedIn} from "./Service/AuthService";
import ResetPasswordUpdate from "./Component/ResetPasswordUpdate/ResetPasswordUpdate";
import TwoFactorAuth from "./Component/TwoFactorAuth/twoFactorAuth";
import 'react-loading-skeleton/dist/skeleton.css'
import {SkeletonTheme} from "react-loading-skeleton";

function App() {

    function AdminGuardedRoute({element}) {
        if (!isUserLoggedIn() || !isAdministrator()) {
            return <Navigate to="/404"/>
        }
        return element;
    }

    function LoggedUserGuardedRoute({element}) {
        if (isUserLoggedIn()) {
            return <Navigate to="/404"/>
        }
        return element;
    }

    function NotLoggedUserGuardedRoute({element}) {
        if (!isUserLoggedIn()) {
            return <Navigate to="/404"/>
        }
        return element;
    }

    return (
        <>
            <SkeletonTheme baseColor="var(--myWhite)" highlightColor="#8BDDF3FF">
                <BrowserRouter>
                    <Header/>
                    <Routes>
                        <Route path='/' element={<> <Hero/> <About/> <Partners/> <Pricing/> <Accounts/>  </>}></Route>
                        <Route path='/about' element={<About/>}></Route>
                        <Route path='/pricing' element={<Pricing/>}></Route>
                        <Route path='/accounts' element={<Accounts/>}></Route>
                        <Route path='/partners' element={<Partners/>}></Route>
                        <Route path='/risk-disclousure-terms-conditions' element={<RiskAndTerms/>}></Route>
                        <Route path='/community' element={<Community/>}></Route>
                        <Route path='/500' element={<Error500/>}></Route>
                        <Route path='/404' element={<Error404/>}></Route>
                        <Route path="*" element={<Navigate to="/404"/>}/>
                        <Route path="/admin" element={<AdminGuardedRoute element={<Admin/>}/>}/>
                        <Route path='/auth/login' element={<LoggedUserGuardedRoute element={<Login/>}/>}/>
                        <Route path='/auth/register' element={<LoggedUserGuardedRoute element={<Register/>}/>}/>
                        <Route path='/auth/login/two-factor-auth' element={<LoggedUserGuardedRoute element={<TwoFactorAuth/>}/>}/>
                        <Route path='/reset-password' element={<LoggedUserGuardedRoute element={<ResetPassword/>}/>}/>
                        <Route path='/reset-password-update' element={<LoggedUserGuardedRoute element={<ResetPasswordUpdate/>}/>}/>
                        <Route path='/change-password' element={<NotLoggedUserGuardedRoute element={<ChangePassword/>}/>}/>
                    </Routes>
                    <Footer/>
                </BrowserRouter>
            </SkeletonTheme>
        </>
    );
}

export default App;
