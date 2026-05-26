import React from "react";
import "./DeviceBooleanAttribute.css"
import parse from "html-react-parser"
class DeviceBooleanAttribute extends React.Component{

    constructor(props)
    {
        super(props);
        this.state={};
        this.generateValueContent = this.generateValueContent.bind(this);
        this.generateValueStyle = this.generateValueStyle.bind(this);
        this.generateDisplayValueContent = this.generateDisplayValueContent.bind(this);
    }
    // {(this.props.attribute.value.value)?"true":"false"}
    generateValueContent()
    {
        if(this.props.attribute.value!==undefined && this.props.attribute.value!=null)
        {
            if(this.props.attribute.value.value!==undefined && this.props.attribute.value.value!=null)
            {
                if( this.props.attribute.value.value=== true)
                {
                    return <div className="booleanAttributeValueTrue"  />;
                }
                else
                {
                    return <div className ="booleanAttributeValueFalse"  />;

                }
            }

        }
        return "";
    }
    generateDisplayValueContent()
    {
        if(this.props.attribute.value!==undefined && this.props.attribute.value!=null)
        {
            if(this.props.attribute.value.displayValue!==undefined && this.props.attribute.value.displayValue!=null)
            {
                return parse(this.props.attribute.value.displayValue.toString());
            }

        }
        return "";
    }
    generateValueStyle()
    {
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
    }
    //{(this.props.attribute.value.displayValue.toString()) }
    render()
    {
        return(
            <div className="booleanAttributeMainDiv">
                            <div className="booleanAttributeNameDiv">
                            {this.props.attribute.name.toString()+":"} &nbsp;
                                </div>
                            <div className="booleanAttributeValueDiv" style={this.generateValueStyle()}>
                                {this.generateValueContent()}
                                </div>
                                <div className="booleanAttributeDisplayValueDiv">
                            { this.generateDisplayValueContent() }
                                </div>
              
            </div>
        );
    }

}

export default DeviceBooleanAttribute;