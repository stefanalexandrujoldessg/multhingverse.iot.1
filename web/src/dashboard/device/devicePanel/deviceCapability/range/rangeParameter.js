import React from "react";
import "./rangeParameter.css"
//import { Button } from "react-bootstrap";
import {Form} from "react-bootstrap";

class RangeParameter extends React.Component{

    constructor(props)
    {
        super(props);
        this.state = {
            value: 0,
             
        };
        
  
this.sendValue = this.sendValue.bind(this);
 this.generateRangeMax = this.generateRangeMax.bind(this);
 this.generateRangeMin = this.generateRangeMin.bind(this);
this.handleRangeInput = this.handleRangeInput.bind(this);
this.generateRangeStep = this.generateRangeStep.bind(this);
    }
 
    componentDidMount()
    {
        //corelation if you thisnk it would be ok
        this.sendValue(this.state.value);
    }
    sendValue(value,shouldUpdate)
    {
        if(this.props.setParameterValue!==undefined && this.props.setParameterValue!==null)
        {
            //specialconsole.log({value:{value:value}});
            this.props.setParameterValue(this.props.parameter.id,  {value:value}, shouldUpdate);
        }
    }
   


     

    handleRangeInput(event){
       let value = parseFloat(event.target.value);
        this.sendValue( value, true);
        if(value !== this.state.value)
        {
        this.setState({value:event.target.value});
        }
    }

    generateRangeMin()
    {
        if(this.props.parameter.rangeMin!==undefined && this.props.parameter.rangeMin!=null)
        {
            return this.props.parameter.rangeMin;
        }
        return 0;

    }
    generateRangeMax()
    {
        if(this.props.parameter.rangeMax!==undefined && this.props.parameter.rangeMax!=null)
        {
            return this.props.parameter.rangeMax;
        }
        return 100;
    }
    generateRangeStep()
    {
        if(this.props.parameter.rangeStep!==undefined && this.props.parameter.rangeStep!=null)
        {
            return this.props.parameter.rangeStep;
        }
        return 1;
    }
    render()
    {
        return(
            <>
             
             <Form.Group className = "inputRangeFormGroup">
             <Form.Range className = "inputRange" onInput={this.handleRangeInput} value={this.state.value} min = {this.generateRangeMin()} max = {this.generateRangeMax()} step = {this.generateRangeStep()}/>
                </Form.Group>
                <div className="rangeLimitDiv">
                    {this.state.value}
                </div>
                





                
            </>
        )
    }
}
export default RangeParameter;