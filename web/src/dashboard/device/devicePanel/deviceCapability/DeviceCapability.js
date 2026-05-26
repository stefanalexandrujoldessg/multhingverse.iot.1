import React from "react";
import "./DeviceCapability.css"
import BoolParameter from "./../deviceCapability/bool/boolParameter.js"
import NumberParameter from "./number/numberParameter";
import RangeParameter from "./range/rangeParameter";
import StringParameter from "./string/stringParameter";
import ColorParameter from "./color/colorParameter";
class DeviceCapability extends React.Component{

    constructor(props)
    {
        super(props);
        this.state={};
        this.parameters={};
        this.setParameterValue = this.setParameterValue.bind(this);
        this.sendCapabilityMessage = this.sendCapabilityMessage.bind(this);
    }
    // {(this.props.attribute.value.value)?"true":"false"}
   

    setParameterValue(paramId,value,shouldSend)
    {
        //specialconsole.log(paramId, value);
        if(this.parameters[paramId]!==undefined && this.parameters[paramId]!==null)
        {
            this.parameters[paramId].value = value;
        }
        //specialconsole.log("setParameterValue "+ JSON.stringify(this.parameters));
        if(shouldSend!==undefined && shouldSend!==null && shouldSend === true)
        {
            if(Object.keys(this.parameters).length===1 && this.parameters[paramId].autoSendOnUpdate!== undefined&& this.parameters[paramId].autoSendOnUpdate!== null&& this.parameters[paramId].autoSendOnUpdate=== true)
            {
            this.sendCapabilityMessage();
            }
        }
    }
    sendCapabilityMessage()
    {
        let capability = {
            
        };
        let parameters = {};
        if(this.props.capability.id!== undefined && this.props.capability.id!== null)
        {


            if(this.props.capability.parameters!== undefined && this.props.capability.parameters !== null)
            {
                let key;
                for (key   in this.props.capability.parameters)
                {
                    if(this.props.capability.parameters[key]!== undefined && this.props.capability.parameters[key]!== null)
                    {
                        parameters[key] = {value:this.parameters[key].value};
                    }
                }
                capability[this.props.capability.id]={
                    parameters:parameters
                };
                if(this.props.sendCapability!==undefined && this.props.sendCapability!==null)
                {
                     
                    this.props.sendCapability(capability);
                }

            }
        }
        

        

    }
    generateParameterDivContent()
    {

        let result = [];
        if(this.props.capability!== undefined && this.props.capability !== null)
        {
            if(this.props.capability.parameters!== undefined && this.props.capability.parameters !== null)
            {
                let key;
                for (key   in this.props.capability.parameters)
                {
                    if(this.props.capability.parameters[key]!== undefined && this.props.capability.parameters[key]!== null)
                    {
                        if(this.props.capability.parameters[key].metaType!== undefined && this.props.capability.parameters[key].metaType!== null)
                        {                        
                            if(this.props.capability.parameters[key].type!== undefined && this.props.capability.parameters[key].type!== null)
                            {
                                if(this.props.capability.parameters[key].metaType.toLowerCase()==="json")//&& this.props.capability.parameters[key].metaType!== null)
                                {
                                    if(this.props.capability.parameters[key].type.toLowerCase()==="bool"||this.props.capability.parameters[key].type.toLowerCase()==="boolean")//&& this.props.capability.parameters[key].metaType!== null)
                                    {
                                        
                                        let parameter = this.props.capability.parameters[key];
                                        parameter.id = key;
                                        if(this.parameters[key] === undefined || this.parameters[key] === null)
                                        {   
                                            this.parameters[key] = {};//?? should I yes
                                            this.parameters[key].value = null;
                                            this.parameters[key].autoSendOnUpdate = true;
                                        }

                                            result.push(<div className="capabilityParameterDiv">
                                                            <div className="capabilityParameterNameDivLineDiv">
                                                                {this.props.capability.parameters[key].name}   
                                                            </div>
                                                            <BoolParameter state = {this.props.state}parameter = {parameter} setParameterValue = {this.setParameterValue}/>
                                                         </div>)
                                    }


                                    if(this.props.capability.parameters[key].type.toLowerCase()==="number"||this.props.capability.parameters[key].type.toLowerCase()==="number")//&& this.props.capability.parameters[key].metaType!== null)
                                    {
                                        
                                        let parameter = this.props.capability.parameters[key];
                                        parameter.id = key;
                                        if(this.parameters[key] === undefined || this.parameters[key] === null)
                                        {   
                                            this.parameters[key] = {};//?? should I yes
                                            this.parameters[key].value = null;
                                        }

                                            result.push(<div className="capabilityParameterDiv">
                                                            <div className="capabilityParameterNameDivLineDiv">
                                                                {this.props.capability.parameters[key].name}   
                                                            </div>
                                                            <NumberParameter  state = {this.props.state} parameter = {parameter} setParameterValue = {this.setParameterValue}/>
                                                         </div>)
                                    }


                                    if(this.props.capability.parameters[key].type.toLowerCase()==="string"||this.props.capability.parameters[key].type.toLowerCase()==="string")//&& this.props.capability.parameters[key].metaType!== null)
                                    {
                                        
                                        let parameter = this.props.capability.parameters[key];
                                        parameter.id = key;
                                        if(this.parameters[key] === undefined || this.parameters[key] === null)
                                        {   
                                            this.parameters[key] = {};//?? should I yes
                                            this.parameters[key].value = null;
                                        }

                                            result.push(<div className="capabilityParameterDiv">
                                                            <div className="capabilityParameterNameDivLineDiv">
                                                                {this.props.capability.parameters[key].name}   
                                                            </div>
                                                            <StringParameter  state = {this.props.state} parameter = {parameter} setParameterValue = {this.setParameterValue}/>
                                                         </div>)
                                    }




                                }
                                if(this.props.capability.parameters[key].metaType.toLowerCase()==="custom")//&& this.props.capability.parameters[key].metaType!== null)
                                {
                                    if(this.props.capability.parameters[key].type.toLowerCase()==="range"||this.props.capability.parameters[key].type.toLowerCase()==="range")//&& this.props.capability.parameters[key].metaType!== null)
                                    {
                                        
                                        let parameter = this.props.capability.parameters[key];
                                        parameter.id = key;
                                        if(this.parameters[key] === undefined || this.parameters[key] === null)
                                        {   
                                            this.parameters[key] = {};//?? should I yes
                                            this.parameters[key].value = null;
                                            this.parameters[key].autoSendOnUpdate = true;
                                        }

                                            result.push(<div className="capabilityParameterDiv">
                                                            <div className="capabilityParameterNameDivLineDiv">
                                                                {this.props.capability.parameters[key].name}   
                                                            </div>
                                                            <RangeParameter  state = {this.props.state} parameter = {parameter} setParameterValue = {this.setParameterValue}/>
                                                         </div>)
                                    }
                                    if(this.props.capability.parameters[key].type.toLowerCase()==="color"||this.props.capability.parameters[key].type.toLowerCase()==="color")//&& this.props.capability.parameters[key].metaType!== null)
                                    {
                                        
                                        let parameter = this.props.capability.parameters[key];
                                        parameter.id = key;
                                        if(this.parameters[key] === undefined || this.parameters[key] === null)
                                        {   
                                            this.parameters[key] = {};//?? should I yes
                                            this.parameters[key].value = null;
                                            this.parameters[key].autoSendOnUpdate = true;
                                        }

                                            result.push(<div className="capabilityParameterDiv">
                                                            <div className="capabilityParameterNameDivLineDiv">
                                                                {this.props.capability.parameters[key].name}   
                                                            </div>
                                                            <ColorParameter  state = {this.props.state} parameter = {parameter} setParameterValue = {this.setParameterValue}/>
                                                         </div>)
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        return result;
    }
    generateNameDivContent()
    {
        if(this.props.capability!== undefined && this.props.capability !== null)
        {
            return this.props.capability.name+" ";//+this.props.capability.id;
        }
    }
    render()
    {
        return(
            <div className="capabilityMainDiv">
               
                            <div className="capabilityNameDiv">
                            {this.generateNameDivContent()}
                                </div>
                                 {this.generateParameterDivContent()}
                               
                              
                                <div className="capabilityButtonDiv"  >
                                    <button onClick={this.sendCapabilityMessage} className="capabilityExecuteButton"> </button>
                                </div>
                                 
              
            </div>
        );
    }

}

export default DeviceCapability;