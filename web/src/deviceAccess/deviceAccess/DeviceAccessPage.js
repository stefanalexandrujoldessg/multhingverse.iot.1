import "./DeviceAccessPage.css"
import * as Rest_Api from "./../api/DevicesAccessRestClient"
import React from "react"

class DeviceAccessPage extends React.Component
{
    constructor(props)
    {
        super(props);
        this.state = {accessUsers:[]};
        this.textInputRef = React.createRef();
        this.deviceNameRef = React.createRef();

        this.callback = this.callback.bind(this);
        this.generateAccessUsersContent = this.generateAccessUsersContent.bind(this);
        this.removeAccessUser = this.removeAccessUser.bind(this);
        this.addAccessUser = this.addAccessUser.bind(this);
        this.callbackAlter = this.callbackAlter.bind(this);
        this.generateDeviceName = this.generateDeviceName.bind(this);
        this.setDeviceName = this.setDeviceName.bind(this);
        this.callbackSetName = this.callbackSetName.bind(this);
        this.deleteDevice = this.deleteDevice.bind(this);
        this.callbackDeleteDevice = this.callbackDeleteDevice.bind(this);
        
    }
    componentDidMount()
    { 
        try{
        window.scrollTo(0,0);
        }
        catch(E)
        {}
        if(this.props.device!==undefined && this.props.device !== null)
        {
            Rest_Api.getDeviceAccessUsers(this.props.device.id, this.callback);
        }
            
    }
    generateDeviceName()
    {
        if(this.props.device!==undefined && this.props.device!==null)
        {
                if(this.props.device.name !==undefined && this.props.device.name!== null)
                {
                        return this.props.device.name;
                }
                if(this.props.device.id !==undefined && this.props.device.id!== null)
                {
                        return this.props.device.id;
                }
                
        }
        return "unknown identiy"
    }
    callback(response, status, error)
    {   console.log(response);

        if ( response!== undefined &&  response!== null)
        {
            if( response.accessUsers!== undefined && response.accessUsers!==null)
            {
                let key;
                let accessUsers = [];
                for(key in response.accessUsers)
                {
                    let obj = {id:key, username: response.accessUsers[key]};
                    console.log(obj);
                    accessUsers.push(obj);
                }
                console.log(" asss " + JSON.stringify(accessUsers));

                this.setState({accessUsers:accessUsers});
                console.log(" as " + JSON.stringify(this.state));
            }
        }

    }
    generateAccessUsersContent(){
        let response = [];
        for (let key in this.state.accessUsers)
        {
            response.push(<div className="deviceAccessPageUserDiv" onClick = {()=>this.removeAccessUser(this.state.accessUsers[key].username)}>{this.state.accessUsers[key].username}</div>);

        }
        return response;
    }
    removeAccessUser(username)//\ 
    {
        if(this.props.device!==undefined && this.props.device !== null)
        {
            Rest_Api.removeByUsername(this.props.device.id, username, this.callbackAlter);
        }    }
        addAccessUser()
        {
            if(this.textInputRef.current!== undefined && this.textInputRef.current!==null)
            {
                if(this.props.device!==undefined && this.props.device !== null)
        {
            Rest_Api.addByUsername(this.props.device.id, this.textInputRef.current.value, this.callbackAlter);
        }  
            }
        }
        setDeviceName()
        {
            if(this.deviceNameRef.current!== undefined && this.deviceNameRef.current!==null)
            {
                if(this.props.device!==undefined && this.props.device !== null)
        {
            this.name  = this.deviceNameRef.current.value.toString();
            let userId =window.localStorage.getItem("id");
            Rest_Api.postUserDeviceConfiguration(userId,this.props.device.id, JSON.stringify({name:this.deviceNameRef.current.value}), this.callbackSetName);
        }  
            }
        }
        deleteDevice()
        {
             
                if(this.props.device!==undefined && this.props.device !== null)
        {
            
             
            Rest_Api.deleteDevice(this.props.device.id, this.callbackDeleteDevice);
        }  
             
        }
        callbackDeleteDevice(response,status,error)
        {
            console.log(response,status,error);
            if(status ===200)
            {
                //his.props.refetchDevices();
            }
        }
        callbackSetName(response,status,error)
        {
            console.log(response,status,error);
            if(status ===200)
            {
                //his.props.refetchDevices();
            }
        }
    callbackAlter(response,status,error)
    {
        if(this.props.device!==undefined && this.props.device !== null)
        {
            Rest_Api.getDeviceAccessUsers(this.props.device.id, this.callback);
        }    }
    render()
    {
        return (
        <div className="deviceAccessPageMaiDiv">   
            <div className="deviceAccessPageDeviceDiv">
                {this.generateDeviceName()}
                {this.props.admin === true &&
                <div>
                <input type = "text" ref = {this.textInputRef} className ="deviceAccessPageTextInput"/>
                <button  className ="deviceAccessPageButton" onClick = {this.addAccessUser}> Add access user</button>
                 
                </div>}
            </div>
            
            <div className="deviceAccessPageDeviceDiv">
                <div></div>
                <div>
                

                <input type = "text" ref = {this.deviceNameRef} className ="deviceAccessPageTextInput"/>
                <button  className ="deviceAccessPageButton" onClick = {this.setDeviceName}>Set device name</button>
                 
                </div>
            </div>
            {this.props.admin === true && 
            <div className="deviceAccessPageDeviceDiv">
                <div></div>
                <div>
                <button  className ="deviceAccessPageButton" onClick = {this.deleteDevice}>Delete</button>

               
                 
                </div>
            </div>
            
             }


            <div className="deviceAccessPageUsersDiv">
                {this.props.admin === true && this.generateAccessUsersContent()}
            </div>
            
        </div>);
    }
}
export default DeviceAccessPage;