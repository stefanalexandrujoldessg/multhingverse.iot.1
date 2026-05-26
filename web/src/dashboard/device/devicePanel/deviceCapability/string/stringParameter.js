import React from "react";
import "./stringParameter.css"
//import { Button } from "react-bootstrap";
import {Form, DropdownButton, Dropdown, InputGroup} from "react-bootstrap";

class StringParameter extends React.Component{

    constructor(props)
    {
        super(props);
        this.state = {
            value: "",
            showCorelationVariables: true,
            corelationList:[],
            formControls:{
                inputString:
                {
                    name:"inputString",
                    value:"",
                    valid:true,
                    validationError:"X"
                }
            }
        };
        
this.setValue = this.setValue.bind(this);
this.switchChangeHandler = this.switchChangeHandler.bind(this);
this.sendValue = this.sendValue.bind(this);
this.handleFormTextInputChange = this.handleFormTextInputChange.bind(this);
this.resolveCorelation = this.resolveCorelation.bind(this);
this.generateCorelationList = this.generateCorelationList.bind(this);
this.handleCorelationElementClick = this.handleCorelationElementClick.bind(this);
    }
 
    componentDidMount()
    {
        //corelation if you thisnk it would be ok
        this.sendValue(this.state.value);
        this.resolveCorelation();
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
      
        //this.sendValue(this.state.value);
    }
    sendValue(value,shouldUpdate)
    {
        if(this.props.setParameterValue!==undefined && this.props.setParameterValue!==null)
        {
            //specialconsole.log({value:{value:value}});
            this.props.setParameterValue(this.props.parameter.id,  {value:value});
        }
    }
    setValue(value)
    {  this.sendValue(value);
        this.setState({value:value});
    }
    switchChangeHandler()
    {   
            //specialconsole.log(this.state);
            this.setValue(!this.state.value);
        

    } 


    handleFormTextInputChange(event)
    {
            let name = event.target.name;
            let value = event.target.value;
            if(name!==undefined && name !== null )
            {   //specialconsole.log(name);
                    let updatedFormControls = this.state.formControls;
                    let updatedFormElement = updatedFormControls[name];
                    updatedFormElement.value = value;
             
                    updatedFormElement.valid = (value!== null);//&& (!isNaN(parseFloat(value)));
                    updatedFormControls[name] = updatedFormElement;
                    if(updatedFormElement.valid===true)
                    {
                        this.setValue(value);
                    }
            
                    this.setState({
                        formControls: updatedFormControls,
                       /////////// formIsValid: formIsValid
                    });
            

            }
            //specialconsole.log(this.state);
    }
    resolveCorelation()
    {
        let result = [];
        
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
                        if( this.props.state[this.props.parameter.corelationAttribute].type!== undefined && this.props.state[this.props.parameter.corelationAttribute].type!== null &&  this.props.state[this.props.parameter.corelationAttribute].type ==="list" )
                        {
                         //if(this.state.value!==this.props.state[this.props.parameter.corelationAttribute].value.value)
                        // {
                            //specialconsole.log();
                         this.setState({corelationList:this.props.state[this.props.parameter.corelationAttribute].value.value});}
                       // }
                     }
                 }
                  
             }
         }
 
     }
    }
    }
    handleCorelationElementClick(value)
    {
        //specialconsole.log(value);
        let stateCopy = this.state.formControls;
        stateCopy.inputString.value = value;
        this.setValue(value);
        this.setState({formControls:stateCopy});
        //specialconsole.log(this.state);
    }
    generateCorelationList()
    {
        let result = [];
        let key;
        for ( let key in this.state.corelationList)
        {   
            result.push (<div className = "inputStringCorelationElement" onClick = {()=>{this.handleCorelationElementClick(this.state.corelationList[key].toString())}} > {this.state.corelationList[key].toString()}</div>)
            
        }
        return result;
    }
    render()
    {
        return(
            <>
            
             <Form.Group className= "inputStringFormGroup">
            
            <Form.Control
                    type="username"
                    className="inputString"
                    aria-describedby="inputStringHelpBlock"
                    defaultValue={this.state.formControls.inputString.value}
                     value = {this.state.formControls.inputString.value}
                    valid = {this.state.formControls.inputString.valid?1:0}
                    name = {this.state.formControls.inputString.name}
                    onChange = {this.handleFormTextInputChange}

                />
                <Form.Text id="inputStringHelpBlock" muted>
                {(!this.state.formControls.inputString.valid)&& this.state.formControls.inputString.validationError}
                </Form.Text>

             
                </Form.Group>
                
                <button className="inputStringDropdownDiv" onClick={()=>{this.setState({showCorelationVariables:!this.state.showCorelationVariables})}}>></button>
                {(this.state.showCorelationVariables===true)&& this.generateCorelationList()}
                





                
            </>
        )
    }
}
export default StringParameter;