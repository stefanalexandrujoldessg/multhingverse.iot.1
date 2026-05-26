import React from "react";
import {Navigate} from "react-router-dom"
import "./HomePage.css"
import TopBar from "./../topbar/TopBar"
class HomePage extends React.Component
{
    constructor(props)
    {
        super(props);

        this.state =
        {
            redirect:false,
            redirectTo: ""
        }
        this.redirectTo = this.redirectTo.bind(this);
    }
    redirectTo(path)
    {
        this.setState(
            {
                redirect:true,
                redirectTo: path
            }
        )

    }
    render()
        {
            return (
               
            <div className="homePageMainDiv">
                 {this.state.redirect === true && <Navigate to ={this.state.redirectTo}/>}
                <TopBar/>
                <div className="homePageBodyDiv">


                    <div className="homePageButtonsDiv">
                    <button onClick = {()=>this.redirectTo("/dashboard")}className = "homePageButton">
                           Dashboard
                        </button>
                        <button onClick = {()=>this.redirectTo("/devicesAccess")}className = "homePageButton">
                            Edit devices access
                        </button>
                        <button onClick = {()=>this.redirectTo("/login")}className = "homePageButton">
                            Log In
                        </button>
                       <div className="homeDivLineBreakDiv"/>
                        <button onClick = {()=>this.redirectTo("/login")}className = "homePageButton">
                            Log Out
                        </button>
                    </div>
                </div>
                
            </div>
        
    
            );
    }
}

export default HomePage;