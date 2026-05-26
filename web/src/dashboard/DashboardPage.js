import React from "react";
import TopBar from "../topbar/TopBar";
import WebSocketWrapper from "./../utils/websoket/WebSocketWrapper";

import {Navigate} from "react-router-dom";
import {HOST} from "./../utils/host/Hosts"
import DevicePanel from "./device/devicePanel/DevicePanel"
import "./DashboardPage.css"
import DeviceFocusPanel from "./device/devicePanel/DeviceFocusPanel";
class DashboardPage extends React.Component{

    constructor(props){
        super(props);

        this.state = {
            redirect : false,
            redirectTo: "",
            devices:
            {

            },
            deviceFocus: false,
            deviceFocusId: null,
        };
        this.nrOfMessages = 0;
        this.numberOfAuthorizationRequests = 0;
        this.webSocketMessageHandler = this.webSocketMessageHandler.bind(this);
        this.webSocketClient = undefined;//new WebSocketWrapper(HOST.humanMicroserviceApi.host,HOST.humanMicroserviceApi.proxyPath+HOST.humanMicroserviceApi.webSocketPath, false, this.webSocketMessageHandler);
        this.recursiveStateMappingSetter = this.recursiveStateMappingSetter.bind(this);
        this.generateDevicesPanels = this.generateDevicesPanels.bind(this);
        this.sendCapability = this.sendCapability.bind(this);
        this.setDeviceFocus = this.setDeviceFocus.bind(this);
        this.unsetDeviceFocus = this.unsetDeviceFocus.bind(this);
    }
    componentDidMount()
    {
        this.webSocketClient = new WebSocketWrapper(HOST.humanMicroserviceApi.host,HOST.humanMicroserviceApi.proxyPath+HOST.humanMicroserviceApi.webSocketPath, HOST.humanMicroserviceApi.secure, this.webSocketMessageHandler,()=>{            this.webSocketClient.send(JSON.stringify({type:"authorization", authorization  : "Bearer "+window.localStorage.getItem("token")})) });

       this.webSocketClient.open();
       this.webSocketClient.initializeHeartbeat();
    }
    componentWillUnmount()
    {
       this.webSocketClient.close();
    }
    webSocketMessageHandler(message)
    {
        //specialconsole.log("[Dashboard:] message: "+message+" "+JSON.parse(message).type);
        this.nrOfMessages = this.nrOfMessages+1;
        //specialconsole.log("nr of messages "+ this.nrOfMessages);
        let messageObject = JSON.parse(message);
        if(messageObject.type === "requestAuthorization")
        {
            this.webSocketClient.send(JSON.stringify({type:"authorization", authorization  : "Bearer "+window.localStorage.getItem("token")}));
            this.numberOfAuthorizationRequests = this.numberOfAuthorizationRequests+1;
            if(this.numberOfAuthorizationRequests>=2)
            {
                this.setState({redirect:true, redirectTo:"/login"});
            }
        }

        if(messageObject.type === "devices")
        {
            
            let stateDevicesCopy = JSON.parse(JSON.stringify(this.state.devices));
            this.recursiveStateMappingSetter(messageObject.devices,stateDevicesCopy,true);
            console.log(stateDevicesCopy);
            this.setState({devices: stateDevicesCopy});

            //specialconsole.log(this.state);
            this.numberOfAuthorizationRequests = 0;

            //this.webSocketClient.send(JSON.stringify({type:"authorization", authorization  : "Bearer "+window.localStorage.getItem("token")}));
        }
    }


    sendCapability(deviceId, capability)
    {
        let message = {
            type: "deviceCapability",
            deviceCapability:{
                id:deviceId,
                capability:capability
            }
        };
        //specialconsole.log("sendCApability "+JSON.stringify(message));
        this.webSocketClient.send(JSON.stringify(message));

    }

    recursiveStateMappingSetter(newState,stateCopy, forceInsert)
    {
        let primitive = true;
        for(let key in newState)
        { primitive = false;
            //specialconsole.log(key);
            if((forceInsert === true) ||stateCopy.hasOwnProperty(key))
            {
                //specialconsole.log("has property");
            if(stateCopy[key] === undefined || stateCopy[key] === null || typeof(stateCopy[key]) !== 'object' || Array.isArray(stateCopy[key]))
            {                 

                //specialconsole.log("Null "+key+ stateCopy[key]);
                if(newState[key] === null)
                    {
                        delete stateCopy[key];
                    }
                    else{
                    stateCopy[key] = newState[key];
                    }
                //specialconsole.log("Null "+ stateCopy[key]);

            }
            else{
                
                if( this.recursiveStateMappingSetter(newState[key], stateCopy[key], forceInsert))
                {
                    //cred ca aici nu mai ajungi
                    //ba ajungi cand in loc de o bval care e un objn se specifica iun newStatre o primitivqa
                    //specialconsole.log("Primitive: "+ key);
                    if(newState[key] === null)
                    {
                        delete stateCopy[key];
                    }
                    else{
                    stateCopy[key] = newState[key];
                    }
                }
                

            }
            }
           
        }
       return primitive;
    }

    setDeviceFocus(deviceId)
    {
        this.setState({deviceFocus: true, deviceFocusId: deviceId});
    }
    unsetDeviceFocus(deviceId)
    {
        this.setState({deviceFocus: false, deviceFocusId: null});
    }
    generateDevicesPanels()
    {
        if(this.state.deviceFocus === true)
        {
            if(this.state.deviceFocusId !== undefined && this.deviceFocusId !== null)
            {
                if( this.state.devices[this.state.deviceFocusId]!=undefined && this.state.devices[this.state.deviceFocusId]!=null)
                {
                    let result = [];
                    let deviceProp = JSON.parse(JSON.stringify(this.state.devices[this.state.deviceFocusId]));
                     deviceProp.id = this.state.deviceFocusId;
                    result.push(<DeviceFocusPanel unsetDeviceFocus = {this.unsetDeviceFocus} sendCapability = {this.sendCapability} device ={deviceProp}/>);
                    return result;

                }
            }
        }
        let unsortedList = [];
        for ( let deviceId in this.state.devices)
        {
            let deviceProp = JSON.parse(JSON.stringify(this.state.devices[deviceId]));
            deviceProp.id = deviceId;
            unsortedList.push(deviceProp);

        }
        unsortedList.sort((a,b)=>{
            if(a.online!==undefined && a.online !=null)
            {
                if(b.online!==undefined && b.online !=null)
                {
                    if(a.online===b.online)
                    {
                        return 0;
                    }
                    return (a.online===true)?-1:1;
                }
                else{
                    return -1;
                }
            }else{
                if(b.online!==undefined && b.online !=null)
                {
                    return 1;
                }else{
                    return 0;
                }
            }
            
            
        });
         
        let result = [];
        let key;
        for(key in unsortedList)
        {
            result.push(<DevicePanel  setDeviceFocus = {this.setDeviceFocus} sendCapability = {this.sendCapability} device ={unsortedList[key]}/>);

        }
        /*
        for ( let deviceId in this.state.devices)
        {
            let deviceProp = JSON.parse(JSON.stringify(this.state.devices[deviceId]));
            deviceProp.id = deviceId;

        }
        */
        return result;
    }





    render(){
        return (
            <div className = "dashboardPageMainDiv">
                {(this.state.redirect)&&<Navigate to={this.state.redirectTo}/>}
                <TopBar/>
                            <div className = "dashboardPageDevicesDiv">

                 {this.generateDevicesPanels()}
                </div>
               
            </div>
        );
    }
}
export default DashboardPage;