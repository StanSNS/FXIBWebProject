import Hero from "./Components/Hero/hero";
import About from "./Components/About/about";
import Footer from "./Components/STATIC/Footer/footer";
import Header from "./Components/STATIC/Header/header";
import {BrowserRouter, Navigate, Route, Routes} from 'react-router-dom'

import './globalCSS/css//bootstrap.min.css'
import Pricing from "./Components/Pricing/pricing";
import Partners from "./Components/Partners/Partners";
import Accounts from "./Components/Accounts/Accounts";
import Login from "./Components/Login/Login";
import Register from "./Components/Register/Register";
import Admin from "./Components/Admin/Admin";
import RiskAndTerms from "./Components/RiskAndTerms/RiskAndTerms";
import Community from "./Components/Community/community";
import ChangePassword from "./Components/ChangePassword/changePassword";
import ResetPassword from "./Components/ResetPassword/resetPassword";
import Error500 from "./Components/Errors/500/error500";
import Error404 from "./Components/Errors/404/error404";
import {isAdministrator, isUserLoggedIn} from "./Service/AuthService";
import ResetPasswordUpdate from "./Components/ResetPasswordUpdate/ResetPasswordUpdate";
import TwoFactorAuth from "./Components/TwoFactorAuth/twoFactorAuth";

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
        </>
    );
}

export default App;
