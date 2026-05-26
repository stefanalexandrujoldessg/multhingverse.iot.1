import React from "react";
import {Navigate, Route, Routes} from "react-router-dom"
import "./DevicesAccessPage.css"
import TopBar from "../topbar/TopBar"
import * as Rest_Api from "./api/DevicesAccessRestClient"
import DeviceAccessPage from "./deviceAccess/DeviceAccessPage";

class DevicesAccessPage extends React.Component
{
    constructor(props)
    {
        super(props);

        this.state =
        {
         redirect: false,
            redirectTo: "",
            devicesFetched:false,
            accessDevicesFetched:false,
            devices: {},
            accessDevices:{},
            devicesConfiguration: {},
            accessDevicesConfiguration:{},
            userDevicesConfiguration:{},
            accessDevicesUserDevicesConfiguration:{},
            admin:false
        }
       
        this.redirectTo = this.redirectTo.bind(this);
        this.callbackAdmin = this.callbackAdmin.bind(this);
        this.callbackAccess = this.callbackAccess.bind(this);
        this.fetchAdminDevices = this.fetchAdminDevices.bind(this);
        this.fetchAccessDevices = this.fetchAccessDevices.bind(this);
        this.generateDevicesContent = this.generateDevicesContent.bind(this);
        this.setDevice = this.setDevice.bind(this);
        this.resolveRedirection = this.resolveRedirection.bind(this);
        this.generateAccessDevicesContent = this.generateAccessDevicesContent.bind(this);
    }
    componentDidMount()
    {
        console.log("mount");
        this.fetchAdminDevices();
        this.fetchAccessDevices();

    }
    componentDidUpdate()
    {
        console.log("mupdate");
         if(this.state.redirect===true)
         {
             this.setState({redirect:false})
         }
      //  this.fetchAdminDevices();
    }
    callbackAdmin(response, status, error)
    {
        console.log(response, status, error);
        if(response !== undefined && response!== null)
        {
            if(Array.isArray(response.adminDevices))
            {
                this.setState({devicesFetched: true, devices: response.adminDevices, devicesConfiguration: response.devicesConfiguration, userDevicesConfiguration: response.userDevicesConfiguration});
            }
             
          
             
        }
        
    }
    callbackAccess(response, status, error)
    {
        console.log(response, status, error);
        if(response !== undefined && response!== null)
        {
            if(Array.isArray(response.accessDevices))
            {
                this.setState({accessDevicesFetched: true, accessDevices: response.accessDevices, accessDevicesConfiguration: response.devicesConfiguration, accessDevicesUserDevicesConfiguration: response.userDevicesConfiguration});
            }
             
          
             
        }
        
    }
    fetchAdminDevices()
    {
        Rest_Api.getAdminDevices(this.callbackAdmin);
    }
    fetchAccessDevices()
    {
        Rest_Api.getAccessDevices(this.callbackAccess);
    }
    redirectTo(path)
    {
        
        this.setState(
            {
                redirectr:true,
                redirectTo: path
            }
        )

    }
    setDevice(device, admin)
    {
        this.setState({redirectDevice:device, admin:admin});
        
        this.setState({redirect: true,redirectTo:"deviceAccess"})
    }
    generateDevicesContent()
    {
        let response =[];
        if(this.state.devicesFetched === true)
        {
            if(this.state.devices.length >0)
            {
                let key;
                for(key in this.state.devices)
                {
                    let ok = false;
                    try{
                       // console.log(this.state.devicesConfiguration[this.state.devices[key].toString()]);
                        let configuration = JSON.parse(this.state.devicesConfiguration[this.state.devices[key].toString()]);
                        

                    if(configuration.name!== undefined && configuration.name !== null)
                    {
                        let device ={id: this.state.devices[key], name :configuration.name} ;
                        response.push(<button onClick = {()=> this.setDevice( device ,true)}className="davicesAccessPageButton">{configuration.name }<div className="mutedDiv">{this.state.devices[key]}</div> </button>)
                        ok = true;
                    }}
                    catch(ex)
                    {
                           // alert(ex);
                    }
                    

                    try{
                        // console.log(this.state.devicesConfiguration[this.state.devices[key].toString()]);
                         let userConfiguration = JSON.parse(this.state.userDevicesConfiguration[this.state.devices[key].toString()]);
                         console.log(JSON.stringify(userConfiguration));
 
                     if(userConfiguration.name!== undefined && userConfiguration.name !== null)
                     {
                         let device ={id: this.state.devices[key], name :userConfiguration.name} ;
                         response.pop();
                         response.push(<button onClick = {()=> this.setDevice( device ,true)}className="davicesAccessPageButton">{userConfiguration.name }<div className = "mutedDiv">{this.state.devices[key]}</div> </button>)
                         ok = true;
                     }}
                     catch(ex)
                     {
                             console.log(ex);
                     }


                    if(ok===false)
                    {
                        let device ={id: this.state.devices[key]} ;
                    response.push(<button onClick = {()=> this.setDevice(device )} className="davicesAccessPageButton">{this.state.devices[key]}</button>);
                    }
                }
            }   
        }
        if(response.length <=0)
        {
            response.push(<button className="davicesAccessPageButton">You have no admin devices</button>)

        }
        return response;
    }




    generateAccessDevicesContent()
    {
        let response =[];
        if(this.state.accessDevicesFetched === true)
        {
            if(this.state.accessDevices.length >0)
            {
                let key;
                for(key in this.state.accessDevices)
                {
                    let ok = false;
                    try{
                       // console.log(this.state.devicesConfiguration[this.state.devices[key].toString()]);
                        let configuration = JSON.parse(this.state.accessDevicesConfiguration[this.state.accessDevices[key].toString()]);
                        

                    if(configuration.name!== undefined && configuration.name !== null)
                    {
                        let device ={id: this.state.accessDevices[key], name :configuration.name} ;
                        response.push(<button onClick = {()=> this.setDevice( device )}className="davicesAccessPageButton">{configuration.name }<div className="mutedDiv">{this.state.accessDevices[key]}</div> </button>)
                        ok = true;
                    }}
                    catch(ex)
                    {
                           // alert(ex);
                    }
                    

                    try{
                        // console.log(this.state.devicesConfiguration[this.state.devices[key].toString()]);
                         let userConfiguration = JSON.parse(this.state.accessDevicesUserDevicesConfiguration[this.state.accessDevices[key].toString()]);
                         console.log(JSON.stringify(userConfiguration));
 
                     if(userConfiguration.name!== undefined && userConfiguration.name !== null)
                     {
                         let device ={id: this.state.accessDevices[key], name :userConfiguration.name} ;
                         response.pop();
                         response.push(<button onClick = {()=> this.setDevice( device )}className="davicesAccessPageButton">{userConfiguration.name }<div className = "mutedDiv">{this.state.accessDevices[key]}</div> </button>)
                         ok = true;
                     }}
                     catch(ex)
                     {
                             console.log(ex);
                     }


                    if(ok===false)
                    {
                        let device ={id: this.state.accessDevices[key]} ;
                    response.push(<button onClick = {()=> this.setDevice(device )} className="davicesAccessPageButton">{this.state.accessDevices[key]}</button>);
                    }
                }
            }   
        }
        if(response.length <=0)
        {
            response.push(<button className="davicesAccessPageButton">You have no access devices</button>)

        }
        return response;
    }









    resolveRedirection()
    {   
        
       
        if(this.state.redirect ===true)
        {
        return ( <Navigate to ={this.state.redirectTo}/>);
        }
         
    }
    render()
        {
            return (
                <Routes>
                    <Route path = "/" element = {
                        
            <div className="davicesAccessPageMainDiv">
                {  this.resolveRedirection()}
                 
                <TopBar/>
                <div className="davicesAccessPageBodyDiv">


                    <div className="davicesAccessPageButtonsDiv">
                  {this.generateDevicesContent()}{this.generateAccessDevicesContent()}
                    </div>
                </div> </div> }
                />
              
                
           
            <Route path = "deviceAccess" element = {<DeviceAccessPage admin = {this.state.admin} device = {this.state.redirectDevice}/>}/>

            </Routes>
        
    
            );
    }
}

export default DevicesAccessPage;