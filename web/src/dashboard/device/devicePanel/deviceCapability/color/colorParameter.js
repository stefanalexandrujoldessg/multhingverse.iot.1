import React from "react";
import "./colorParameter.css"
//import { Button } from "react-bootstrap";
import {Form} from "react-bootstrap";

class ColorParameter extends React.Component{

    constructor(props)
    {
        super(props);
        this.state = {
            value: "#a079e6",
             
        };
        
  
this.sendValue = this.sendValue.bind(this);
 
this.handleColorInput = this.handleColorInput.bind(this);
 
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
   


     

    handleColorInput(event){
        //console.log(event.target);
         
       let value = event.target.value.toString();
        this.sendValue( value, true);
        if(value !== this.state.value)
        {
        this.setState({value:event.target.value});
        }
         
    }

    
    
    render()
    {
        return(
            <>
             
             <Form.Group className = "inputColorFormGroup">
             <Form.Control type ="color" className = "inputColor" onInput={this.handleColorInput} value={this.state.value}   />
                </Form.Group>
                 
                





                
            </>
        )
    }
}
export default ColorParameter;