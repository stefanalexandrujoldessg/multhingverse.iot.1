import React from "react";
import "./DeviceStringAttribute.css"
import parse from "html-react-parser"
class DeviceStringAttribute extends React.Component{

    constructor(props)
    {
        super(props);
        this.state={};
        this.generateValueContent = this.generateValueContent.bind(this);
         this.generateDisplayValueContent = this.generateDisplayValueContent.bind(this);
      
    }
    // {(this.props.attribute.value.value)?"true":"false"}
    generateDisplayValueContent()
    {
        if(this.props.attribute.value!==undefined && this.props.attribute.value!=null)
        {
            if(this.props.attribute.value.displayValue!==undefined && this.props.attribute.value.displayValue!=null)
            {
                return parse(this.props.attribute.value.displayValue);
            }

        }
        return "";
    }
    generateValueContent()
    {
        if(this.props.attribute.value!==undefined && this.props.attribute.value!=null)
        {
            if(this.props.attribute.value.value!==undefined && this.props.attribute.value.value!=null)
            {
                
                 
                 
                    return this.props.attribute.value.value.toString();
                
                
            }

        }
        return "";
    }
    
    render()
    {
        return(
            <div className="stringAttributeMainDiv">
                            <div className="stringAttributeNameDiv">
                            {this.props.attribute.name.toString()+":"} &nbsp;
                                </div>
                            <div className="stringAttributeValueDiv" >
                                {this.generateValueContent()}
                                </div>
                                <div className="stringAttributeDisplayValueDiv">
                            { this.generateDisplayValueContent() } 
                                </div>
              
            </div>
        );
    }

}

export default DeviceStringAttribute;