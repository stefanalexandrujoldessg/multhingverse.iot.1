import React from "react";
import {Navigate  } from "react-router-dom";
 
import {Button} from "react-bootstrap";
import "./TopBar.css"
import * as TopBarRestClient from "./api/TopBarRestClient"
class TopBar extends React.Component
{
    constructor(props)
    {
        super(props);

        this.state =
        {
            redirect: false,
            redirectTo: '',
        }
    this.handleLogInSubmit = this.handleLogInSubmit.bind(this);
    this.handleLogOutSubmit = this.handleLogOutSubmit.bind(this);
    this.callbackLogOut = this.callbackLogOut.bind(this);
    this.handleHomeSubmit = this.handleHomeSubmit.bind(this);

    }
    componentDidUpdate()
    {
        if(this.state.redirect===true)
        {
            this.setState({redirect:false});
        }
    }
    handleHomeSubmit()
    {
        this.setState(
            {redirect: true,
            redirectTo:'/'}
        );
    }
    handleLogInSubmit()
    {
        this.setState(
            {redirect: true,
            redirectTo:'/login'}
        );
    }
    handleLogOutSubmit()
    {
            TopBarRestClient.logOut(this.callbackLogOut);
    }
    callbackLogOut(response,status,error)
    {
            //specialconsole.log("[callbackLogOut]" +status);
            if(status===200|| status ===401)
            {
                this.setState(
                    {redirect: true,
                    redirectTo:'/login'}
                );
                    }
    }
    render()
        {
            return (
            <div className="topBarMainDiv"> 

                <div className="topBarSecondaryDiv"> 
                        <Button onClick = {this.handleHomeSubmit} className = "topBarButton" variant = "primary">Home</Button>
                        
                </div>
                <div className="topBarSecondaryDiv topBarFlexGrowDiv"> 
                <Button onClick = { this.handleLogInSubmit} className = "topBarButton topBarRightMargin" variant = "primary" type = "submit">Log In</Button>
                <div className="topBarVerticalBar  " />
                <Button onClick = { this.handleLogOutSubmit} className = "topBarButton"variant = "dark" >Log Out</Button>
                </div>
                {this.state.redirect && <Navigate to = {this.state.redirectTo}/>}
            </div>
        
    
            );
    }
}

export default TopBar;