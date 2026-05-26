import React from "react";
import "./DeviceListAttribute.css"
import parse from "html-react-parser"
class DeviceListAttribute extends React.Component{

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
        let result = [];
        if(this.props.attribute.value!==undefined && this.props.attribute.value!=null)
        {
            if(this.props.attribute.value.value!==undefined && this.props.attribute.value.value!=null)
            {
                let key;
                for(  key in this.props.attribute.value.value)
                {
                
                 
                 
                    result.push(<div className = "listAttributeElementListDiv">{this.props.attribute.value.value[key].toString()}</div>);
                }
                
                
            }

        }
        return result;
    }
    
    render()
    {
        return(
            <div className="listAttributeMainDiv">
                            <div className="listAttributeNameDiv">
                            {this.props.attribute.name.toString()+":"} &nbsp;
                                </div>
                            <div className="listAttributeValueDiv" >
                                {this.generateValueContent()}
                                </div>
                                <div className="listAttributeDisplayValueDiv">
                            { this.generateDisplayValueContent() } 
                                </div>
              
            </div>
        );
    }

}

export default DeviceListAttribute;