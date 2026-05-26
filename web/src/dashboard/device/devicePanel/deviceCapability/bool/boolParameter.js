import React from "react";
import "./boolParameter.css"
//import { Button } from "react-bootstrap";
import {Form} from "react-bootstrap";

class BoolParameter extends React.Component{

    constructor(props)
    {
        super(props);
        this.state = {
            value: false
        };
        
this.setValue = this.setValue.bind(this);
this.switchChangeHandler = this.switchChangeHandler.bind(this);
this.sendValue = this.sendValue.bind(this);
this.resolveCorelation = this.resolveCorelation.bind(this);
    }
/*
 <button className={"boolParameterButton   left "+(this.state.value?"released":"pressed")}><div className="boolParameterSymbol circle"/></button>
            <button className={"boolParameterButton   right "+(this.state.value?"pressed":"released")}><div className="boolParameterSymbol line"/> </button>

       */
componentDidMount()
{
   // this.sendValue(this.state.value); let's do not do that yet
  this.resolveCorelation();
  this.sendValue(this.state.value);
}
componentDidUpdate(prevProps, prevState)
{
    if(prevProps!==this.props)
    {
        //specialconsole.log("props")
        this.resolveCorelation();
    }
    if(prevState!==this.state)
    {
        //specialconsole.log("state")
    }
  
    this.sendValue(this.state.value);

}

resolveCorelation()
{
    if(this.props.state !== undefined && this.props.state !== null )
    {
     if(this.props.parameter !== undefined && this.props.parameter !== null )
     {
         if(this.props.parameter.corelationAttribute !== undefined && this.props.parameter.corelationAttribute !== null )
         {
             if(this.props.state[this.props.parameter.corelationAttribute] !== undefined && this.props.state[this.props.parameter.corelationAttribute] !== null )
             {
                 if(this.props.state[this.props.parameter.corelationAttribute].value !== undefined && this.props.state[this.props.parameter.corelationAttribute].value !== null )
                 {
                     if(this.props.state[this.props.parameter.corelationAttribute].value.value !== undefined && this.props.state[this.props.parameter.corelationAttribute].value.value !== null )
                     {
                        if((typeof this.props.state[this.props.parameter.corelationAttribute].value.value).toString() === "boolean")
                        {
                         if(this.state.value!==this.props.state[this.props.parameter.corelationAttribute].value.value)
                         {
                         this.setState({value:this.props.state[this.props.parameter.corelationAttribute].value.value});}
                     }
                     }
                 }
                  
             }
         }
 
     }
    }
}
sendValue(value,shouldUpdate)
{
    if(this.props.setParameterValue!==undefined && this.props.setParameterValue!==null)
    {
        //specialconsole.log({value:{value:value}});
        this.props.setParameterValue(this.props.parameter.id,  {value:value},shouldUpdate);
    }
}
            setValue(value)
            {  
                this.sendValue(value,true);
                
                this.setState({value:value});
            }
            switchChangeHandler()
            {   
                    //specialconsole.log(this.state);
                    this.setValue(!this.state.value);
              

            } 
    render()
    {
        return(
            <>
            
             
  <Form.Check 
    type="switch"
    id="custom-switch"
    onChange={this.switchChangeHandler}
    checked = {this.state.value}
   />
   
            </>
        )
    }
}
export default BoolParameter;