import React from "react";
import "./numberParameter.css"
//import { Button } from "react-bootstrap";
import {Form} from "react-bootstrap";

class NumberParameter extends React.Component{

    constructor(props)
    {
        super(props);
        this.state = {
            value: 0,
            formControls:{
                inputNumber:
                {
                    name:"inputNumber",
                    value:0,
                    valid:true,
                    validationError:"X"
                }
            }
        };
        
this.setValue = this.setValue.bind(this);
this.switchChangeHandler = this.switchChangeHandler.bind(this);
this.sendValue = this.sendValue.bind(this);
this.handleFormTextInputChange = this.handleFormTextInputChange.bind(this);
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
             
                    updatedFormElement.valid = (value!== null)&& (!isNaN(parseFloat(value)));
                    updatedFormControls[name] = updatedFormElement;
                    if(updatedFormElement.valid===true)
                    {
                        this.setValue((parseFloat(value)));
                    }
            
                    this.setState({
                        formControls: updatedFormControls,
                       /////////// formIsValid: formIsValid
                    });
            

            }
            //specialconsole.log(this.state);
    }


    render()
    {
        return(
            <>
            
             <Form.Group className= "inputNumberFormGroup">
            <Form.Control
                    type="username"
                    className="inputNumber"
                    aria-describedby="inputNumberHelpBlock"
                    defaultValue={this.state.formControls.inputNumber.value}
                     
                    valid = {this.state.formControls.inputNumber.valid?1:0}
                    name = {this.state.formControls.inputNumber.name}
                    onChange = {this.handleFormTextInputChange}

                />
                <Form.Text id="inputNumberHelpBlock" muted>
                {(!this.state.formControls.inputNumber.valid)&& this.state.formControls.inputNumber.validationError}
                </Form.Text>
                </Form.Group>
                





                
            </>
        )
    }
}
export default NumberParameter;