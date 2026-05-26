import React from "react";
import DeviceBooleanAttribute from "./deviceAttribute/bool/DeviceBooleanAttribute";
import DeviceImgAttribute from "./deviceAttribute/img/DeviceImgAttribute";
import DeviceListAttribute from "./deviceAttribute/list/DeviceListAttribute";
import DeviceNumberAttribute from "./deviceAttribute/number/DeviceNumberAttribute"
import DeviceRangeAttribute from "./deviceAttribute/range/DeviceRangeAttribute";
import DeviceStringAttribute from "./deviceAttribute/string/DeviceStringAttribute";
import DeviceVideoAttribute from "./deviceAttribute/video/DeviceVideoAttribute";
import DeviceCapability from "./deviceCapability/DeviceCapability";
import DeviceColorAttribute from "./deviceAttribute/color/DeviceColorAttribute"

import "./DevicePanel.css"
import DeviceFocusPanel from "./DeviceFocusPanel";
class DevicePanel extends React.Component{
    constructor(props)
    {
        super(props);
        this.state={
            backgroundColor: "gray"
        }
        this.generateDevicePanel = this.generateDevicePanel.bind(this);
        this.generateBackgroundColor = this.generateBackgroundColor.bind(this);
        this.generateOnlineBackground = this.generateOnlineBackground.bind(this);
        this.generateOnlineShadow = this.generateOnlineShadow.bind(this);
        this.generateNameDivContent = this.generateNameDivContent.bind(this);
        this.generateBorderColor = this.generateBorderColor.bind(this);
        this.generateMainDivStyle = this.generateMainDivStyle.bind(this);
        this.generateIconShape = this.generateIconShape.bind(this);
        this.generateIconClickAction = this.generateIconClickAction.bind(this);
    }
    generateDevicePanel()
    {
            let result = [];
            for( let attrId in this.props.device.state)
            {
               // result = result+" "+attrId+" " +JSON.stringify(this.props.device.state[attrId].value);
               if(this.props.device.state[attrId]!==null && this.props.device.state[attrId]!==undefined)
               {


                    if(this.props.device.state[attrId].dashboardDisplay!==null && this.props.device.state[attrId].dashboardDisplay!==undefined)
                    {
                        if(this.props.device.state[attrId].dashboardDisplay===true)
                         {
                  
                
                            if(this.props.device.state[attrId].metaType!==undefined&&this.props.device.state[attrId].metaType!==null&&this.props.device.state[attrId].type!==undefined&&this.props.device.state[attrId].type!==null)
                            {
                                if(this.props.device.state[attrId].metaType.toLowerCase()==="json")
                                {
                                    if(this.props.device.state[attrId].type.toLowerCase()==="bool" || this.props.device.state[attrId].type.toLowerCase()==="boolean")
                                    {
                                        //specialconsole.log("[Added attribute]");
                                        let attribute = this.props.device.state[attrId];
                                        attribute.id = attrId;
                                        result.push(<DeviceBooleanAttribute attribute = {attribute}/>)
                                    }
                                    if(this.props.device.state[attrId].type.toLowerCase()==="number" || this.props.device.state[attrId].type.toLowerCase()==="number")
                                    {
                                        //specialconsole.log("[Added attribute]");
                                        let attribute = this.props.device.state[attrId];
                                        attribute.id = attrId;
                                        result.push(<DeviceNumberAttribute attribute = {attribute}/>)
                                    }
                                    if(this.props.device.state[attrId].type.toLowerCase()==="string" || this.props.device.state[attrId].type.toLowerCase()==="string")
                                    {
                                        //specialconsole.log("[Added attribute]");
                                        let attribute = this.props.device.state[attrId];
                                        attribute.id = attrId;
                                        result.push(<DeviceStringAttribute attribute = {attribute}/>)
                                    }
                                }
                                if(this.props.device.state[attrId].metaType.toLowerCase()==="custom")
                                {
                                    if(this.props.device.state[attrId].type.toLowerCase()==="img" || this.props.device.state[attrId].type.toLowerCase()==="image")
                                    {
                                        //specialconsole.log("[Added attribute]");
                                        let attribute = this.props.device.state[attrId];
                                        attribute.id = attrId;
                                        result.push(<DeviceImgAttribute attribute = {attribute}/>)
                                    }
                                    if(this.props.device.state[attrId].type.toLowerCase()==="range" || this.props.device.state[attrId].type.toLowerCase()==="range")
                                    {
                                        //specialconsole.log("[Added attribute]");
                                        let attribute = this.props.device.state[attrId];
                                        attribute.id = attrId;
                                        result.push(<DeviceRangeAttribute attribute = {attribute}/>)
                                    }
                                    if(this.props.device.state[attrId].type.toLowerCase()==="list" || this.props.device.state[attrId].type.toLowerCase()==="list")
                                    {
                                        //specialconsole.log("[Added attribute]");
                                        let attribute = this.props.device.state[attrId];
                                        attribute.id = attrId;
                                        result.push(<DeviceListAttribute attribute = {attribute}/>)
                                    }
                                    if(this.props.device.state[attrId].type.toLowerCase()==="video" || this.props.device.state[attrId].type.toLowerCase()==="video")
                                    {
                                        //specialconsole.log("[Added attribute]");
                                        let attribute = this.props.device.state[attrId];
                                        attribute.id = attrId;
                                        result.push(<DeviceVideoAttribute attribute = {attribute}/>)
                                    }

                                    if(this.props.device.state[attrId].type.toLowerCase()==="color" || this.props.device.state[attrId].type.toLowerCase()==="color")
                                    {
                                        //specialconsole.log("[Added attribute]");
                                        let attribute = this.props.device.state[attrId];
                                        attribute.id = attrId;
                                        result.push(<DeviceColorAttribute attribute = {attribute}/>)
                                    }
                                }

                            }
                        }
                    }
                }

            }
            result.push(  <div className="separatorBorderLineDiv"/>);
            for( let capabId in this.props.device.capabilities)
            {
               // result = result+" "+attrId+" " +JSON.stringify(this.props.device.state[attrId].value);
               if(this.props.device.capabilities[capabId]!==null && this.props.device.capabilities[capabId]!==undefined)
               {
                    if(this.props.device.capabilities[capabId].dashboardDisplay!==null && this.props.device.capabilities[capabId].dashboardDisplay!==undefined)
                    {
                        if(this.props.device.capabilities[capabId].dashboardDisplay===true)
                        {
                                    //specialconsole.log("[Added capability]");
                                    let capability = this.props.device.capabilities[capabId];
                                    capability.id = capabId;
                                    result.push(<DeviceCapability state = {this.props.device.state} //this has to be reengeeniered
                                        sendCapability = {(capability)=>this.props.sendCapability(this.props.device.id,capability)} capability = {capability}/>)
                        }
                    } 
                }

            }


            
        return result;

    }
    generateBackgroundColor()
    {
        if(this.props.device.online!==undefined)
        {
           if(this.props.device.online===true) 
           {
                return "linear-gradient(rgb(0,225,0) , rgb(0,225,0) )";
           }
           else{
               return "linear-gradient(rgb(255,0,0) , rgb(255,0,0) )";

           }
        }
        return "linear-gradient( rgb(0,0,255), rgba(0,0,255)  )";
    }
    generateBorderColor()
    {
        if(this.props.device.online!==undefined)
        {
           if(this.props.device.online===true) 
           {
                return {'--border-main-color':"rgb(0,200,0)",'--border-secondary-color':"rgb(0,255,0)"};
           }
           else{
            return {'--border-main-color':"rgb(200,0,0)",'--border-secondary-color':"rgb(255,0,0)"};


           }
        }
        return     {'--border-main-color':"rgb(158,158,240)",'--border-secondary-color':"rgb(32,32,223)"};

    }
    generateOnlineBackground()
    {
        if(this.props.device.online!==undefined)
        {
           if(this.props.device.online===true) 
           {
                return "linear-gradient(rgb(230,225,230) , rgb(230,225,230) )";//"linear-gradient(217deg, rgba( 0,255,0,.8), rgba(255,0,0,0) 60%)";
           }
           else{
               return "linear-gradient(rgb(230,225,230) , rgb(230,225,230) )";

           }
        }
        return "linear-gradient(217deg, rgba(55,55,55,.8), rgba(255,0,0,0) 60%)";
    }
    generateOnlineShadow()
    {
        if(this.props.device.online!==undefined)
        {
           if(this.props.device.online===true) 
           {
                return "0.05rem 0.15rem 0.5rem 0.01rem rgb(0,150,0)";
           }
           else{
               return "0.05rem 0.15rem 0.5rem 0.01rem rgb(200,0,0)";

           }
        }
        return "0.05rem 0.15rem 0.5rem 0.01rem rgba(0,0,100,0.5)";
    }
    generateIconShape()
    {
        if(this.props.device.online!==undefined)
        {
           if(this.props.device.online===true) 
           {
                return "arrow";
           }
           else{
               return "line";

           }
        }
return "circle";    }
generateIconClickAction()
{
    if(this.props.setDeviceFocus!==undefined)
    {
       if(this.props.setDeviceFocus!==null) 
       {
            return ()=>{console.log("c");this.props.setDeviceFocus(this.props.device.id)};
       }
       else{
           return ()=>{console.log("c");};

       }
    }
return "circle";    }
    generateNameDivContent()
    {
        if(this.props.device.name!==undefined&&this.props.device.name!==null)
        {
            return (  this.props.device.name );
        }
        return "Unknown name"; 
    }
    //style={{backgroundColor : this.generateBackgroundColor()}}
    //style={{background  : this.generateOnlineBackground()}}


    generateMainDivStyle()
    {
        let result = this.generateBorderColor();
        result['--box-shadow-style']= this.generateOnlineShadow();
        return result;
    }
    render()
        {
                    return (
            <div className="devicePanelMainDiv" style = {this.generateMainDivStyle()}>

                <div className="devicePanelHeaderMainDiv">
                    <div className="ellipsis devicePanelHeaderNameDiv">
                        {this.generateNameDivContent()}  
                    </div>
                    <div className="devicePanelHeaderOnlineDiv"   >
                    <i onClick = {this.generateIconClickAction()} className={this.generateIconShape()}></i>
                    
                    </div>
                </div>
             
                <div className="devicePanelBodyMainDiv">
                    

                 
                     
                  
                   {false && <DeviceBooleanAttribute attribute = {{name:"Swit 1",value:{displayValue:"on"}}}/>}
                
                   { this.generateDevicePanel()}
                
                </div>
                 
            </div>
        );
    }
}
export default DevicePanel;