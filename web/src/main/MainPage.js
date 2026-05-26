import React from "react";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import HomePage from "./../home/HomePage";
import LoginPage from "./../login/LoginPage";
import DashboardPage from "./../dashboard/DashboardPage";
import "./MainPage.css";

import DevicesAccessPage from "../deviceAccess/DevicesAccessPage";
import DeviceFocusPanel from "../dashboard/device/devicePanel/DeviceFocusPanel";
import OAuth2CallbackPage from "../login/OAuth2CallbackPage";
class MainPage extends React.Component
{
    constructor(props)
    {
        super(props);

        this.state =
        {
            
        }
    
    }
    componentDidMount()
    {
        document.title="Multhingverse";
    }
    render()
        {
            return (
            <div className="mainPageDiv">
                  
                <BrowserRouter>
                    <Routes>

                    <Route  path = "/" element = {<HomePage/>}/>
               
                    
                    <Route   path = "/login" element = {<LoginPage/>} />
                    <Route path = "/dashboard" element = {<DashboardPage/>}/>
                    <Route path = "/devicesAccess/*" element = {<DevicesAccessPage/>}/>
                    <Route path = "/oauth2/callback" element = {<OAuth2CallbackPage/>}/>

                    </Routes>
                </BrowserRouter>
               
            </div>
        
    
            );
    }
}

export default MainPage;