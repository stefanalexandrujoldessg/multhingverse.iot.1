import React from "react";
import "./DeviceRangeAttribute.css"
import parse from "html-react-parser"
class DeviceRangeAttribute extends React.Component{

    constructor(props)
    {
        super(props);
        this.state={};
        this.generateValueContent = this.generateValueContent.bind(this);
        this.generateValueStyle = this.generateValueStyle.bind(this);
        this.generateDisplayValueContent = this.generateDisplayValueContent.bind(this);
        this.generateRangeMin = this.generateRangeMin.bind(this);
        this.generateRangeMax = this.generateRangeMax.bind(this);

    }
    // {(this.props.attribute.value.value)?"true":"false"}
    generateValueContent()
    {
        if(this.props.attribute.value!==undefined && this.props.attribute.value!=null)
        {
            if(this.props.attribute.value.value!==undefined && this.props.attribute.value.value!=null)
            {
                return this.props.attribute.value.value;
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

                    if(this.props.attribute.rangeMin!==undefined && this.props.attribute.rangeMin!=null)
                    {
                        if(this.props.attribute.rangeMax!==undefined && this.props.attribute.rangeMax!=null)
                        {
                            let full = this.props.attribute.rangeMax - this.props.attribute.rangeMin;
                            let partial = this.props.attribute.value.value - this.props.attribute.rangeMin;
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
                           style["--range-progress"] = procent.toString()+"%";
                        }
                    }
                }
            }
         







                
                
        return style;
    }
    generateRangeMin()
    {
        if(this.props.attribute.rangeMin!==undefined && this.props.attribute.rangeMin!=null)
        {
            return this.props.attribute.rangeMin;
        }
        return "";

    }
    generateRangeMax()
    {
        if(this.props.attribute.rangeMax!==undefined && this.props.attribute.rangeMax!=null)
        {
            return this.props.attribute.rangeMax;
        }
        return "";
    }
    //{(this.props.attribute.value.displayValue.toString()) }
    render()
    {
        return(
            <div className="rangeAttributeMainDiv">
                
                            <div className="rangeAttributeNameDiv">
                                {this.props.attribute.name.toString()+":"} &nbsp;    
                            </div>

                            <div className="rangeAttributeValueDiv" >
                                <div className="rangeLimitDiv">
                                     {this.generateRangeMin()}
                                </div>
                                 <div className="rangeAttributeBaseDiv">
                                    {this.generateValueContent()}
                                    <div className="rangeAttributeProgressDiv" style={this.generateValueStyle()}>
                                    </div>
                                </div>

                                <div className="rangeLimitDiv">
                                {this.generateRangeMax()}
                                </div>  
                            
                            </div>
                                
                            <div className="rangeAttributeDisplayValueDiv">
                                { this.generateDisplayValueContent() }
                            </div>
              
            </div>
        );
    }

}

export default DeviceRangeAttribute;