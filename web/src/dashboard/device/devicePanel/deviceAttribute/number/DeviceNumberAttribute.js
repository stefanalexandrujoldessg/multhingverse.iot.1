import React from "react";
import "./DeviceNumberAttribute.css"
import parse from "html-react-parser"
class DeviceNumberAttribute extends React.Component{

    constructor(props)
    {
        super(props);
        this.state={};
        this.generateValueContent = this.generateValueContent.bind(this);
        this.generateValueStyle = this.generateValueStyle.bind(this);
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
                
                if(  (!isNaN(this.props.attribute.value.value)))
                {
                    return this.props.attribute.value.value;
                }
                else
                {
                    return "nan";

                }
            }

        }
        return "";
    }
    generateValueStyle()
    {
        /*
        let style = {};
        if(this.props.attribute.value!==undefined && this.props.attribute.value!=null)
        {
            if(this.props.attribute.value.value!==undefined && this.props.attribute.value.value!=null)
            {
                if( this.props.attribute.value.value=== true)
                {
                      style['--booleanAttributeValueDiv-padding'] = '0.5rem';//<div style = {{maxWidth:'3rem', width:'1rem', maxHeight:'1rem', height:'3rem',backgroundColor:'rgba(0,255,0,1)', borderRadius:'2rem'}}/>;
                }
                else
                {
                      style['--booleanAttributeValueDiv-padding'] = '0.5rem';//return <div style = {{maxWidth:'3rem', width:'1rem',maxHeight:'1rem', height:'3rem', background:'rgba(255,0,0,1)', borderRadius:'2rem'}}/>;

                }
            }

        }
        return style;
        */
    }
    //{(this.props.attribute.value.displayValue.toString()) }
    render()
    {
        return(
            <div className="numberAttributeMainDiv">
                            <div className="numberAttributeNameDiv">
                            {this.props.attribute.name.toString()+":"} &nbsp;
                                </div>
                            <div className="numberAttributeValueDiv" style={this.generateValueStyle()}>
                                {this.generateValueContent()}
                                </div>
                                <div className="numberAttributeDisplayValueDiv">
                            { this.generateDisplayValueContent() } 
                                </div>
              
            </div>
        );
    }

}

export default DeviceNumberAttribute;