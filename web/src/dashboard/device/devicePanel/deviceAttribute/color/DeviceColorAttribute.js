import React from "react";
import "./DeviceColorAttribute.css"
import parse from "html-react-parser"
class DeviceColorAttribute extends React.Component{

    constructor(props)
    {
        super(props);
        this.state={};
        this.generateValueContent = this.generateValueContent.bind(this);
        this.generateValueStyle = this.generateValueStyle.bind(this);
        this.generateDisplayValueContent = this.generateDisplayValueContent.bind(this);
        this.generateColorMin = this.generateColorMin.bind(this);
        this.generateColorMax = this.generateColorMax.bind(this);

    }
    // {(this.props.attribute.value.value)?"true":"false"}
    generateValueContent()
    {
        if(this.props.attribute.value!==undefined && this.props.attribute.value!=null)
        {
            if(this.props.attribute.value.value!==undefined && this.props.attribute.value.value!=null)
            {
                return <div className="deviceColorAttributeGeneratedValueDiv" style={{background:this.props.attribute.value.value.toString()}}></div>;
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

                    if(this.props.attribute.colorMin!==undefined && this.props.attribute.colorMin!=null)
                    {
                        if(this.props.attribute.colorMax!==undefined && this.props.attribute.colorMax!=null)
                        {
                            let full = this.props.attribute.colorMax - this.props.attribute.colorMin;
                            let partial = this.props.attribute.value.value - this.props.attribute.colorMin;
                            let procent = partial/full*100;
                            
                           procent =  Math.ceil(procent);
                           if(procent<0)
                           {
                               procent = 0;
                           }
                           if(procent>100)
                           {
                               procent = 100;
                           }
                           style["--color-progress"] = procent.toString()+"%";
                        }
                    }
                }
            }
         







                
                
        return style;
    }
    generateColorMin()
    {
        if(this.props.attribute.colorMin!==undefined && this.props.attribute.colorMin!=null)
        {
            return this.props.attribute.colorMin;
        }
        return "";

    }
    generateColorMax()
    {
        if(this.props.attribute.colorMax!==undefined && this.props.attribute.colorMax!=null)
        {
            return this.props.attribute.colorMax;
        }
        return "";
    }
    //{(this.props.attribute.value.displayValue.toString()) }
    render()
    {
        return(
            <div className="colorAttributeMainDiv">
                
                            <div className="colorAttributeNameDiv">
                                {this.props.attribute.name.toString()+":"} &nbsp;    
                            </div>

                            <div className="colorAttributeValueDiv">
                            
                            {this.generateValueContent()}
                                </div>
                                
                            <div className="colorAttributeDisplayValueDiv">
                                { this.generateDisplayValueContent() }
                            </div>
              
            </div>
        );
    }

}

export default DeviceColorAttribute;