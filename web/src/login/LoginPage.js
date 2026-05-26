import React from "react";
import Login from "../test/Login";
import {Form, Button } from "react-bootstrap";
import "./LoginPageStyle.css"
import validate from "./../utils/validator/TextValidator";
import BkImg from "./../resources/loginPageBk.jpg";
import * as LoginRestClient from "./api/LoginRestClient";
import {Navigate} from "react-router-dom";
class LoginPage extends React.Component
{
    constructor(props)
    {
        super(props);

        this.state = {

            formControls:{
                username:
                {
                    name:"username",
                    value:'',
                    valid:false,
                    touched:false,
                    validationError:'At least 5 characters ',
                    validationRules:{
                        minLength:5,
                        isRequired: true,
                         
                    }
                },
                password:
                {
                    name:"password",
                    value:'',
                    valid:false,
                    touched:false,
                    validationError:'At least 7 characters including letters, numbers and special characters',
                    validationRules:
                    {
                        minLength:7,
                        isRequired: true,
                        isPassword: true,
                    }

                }
            },
            formIsValid: false,
            redirect: false,
            redirectTo: "",


        }
        this.handleFormTextInputChange = this.handleFormTextInputChange.bind(this);
        this.handleLoginSubmit = this.handleLoginSubmit.bind(this);
        this.handleSinginSubmit = this.handleSinginSubmit.bind(this);
        this.callbackSinginRequest = this.callbackSinginRequest.bind(this);

        this.callbackLoginRequest = this.callbackLoginRequest.bind(this);
        this.validateLogin = this.validateLogin.bind(this);
        this.callbackValidateLogin = this.callbackValidateLogin.bind(this);
    }
    componentDidMount()
    {
        this.validateLogin(this.callbackValidateLogin);
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
                    updatedFormElement.touched = true;
                    updatedFormElement.valid = validate(value, updatedFormElement.validationRules);
                    updatedFormControls[name] = updatedFormElement;


                    let formIsValid = true;
                    for (let updatedFormElementName in updatedFormControls) {
                        formIsValid = updatedFormControls[updatedFormElementName].valid && formIsValid;
                    }
            
                    this.setState({
                        formControls: updatedFormControls,
                        formIsValid: formIsValid
                    });
            

            }
    }
    handleLoginSubmit()
    {
        if(this.state.formIsValid)
        {
        let username = this.state.formControls.username.value;
        let password = this.state.formControls.password.value;
        //specialconsole.log("[Login] "+username+ " " + password);
        
        let credentials  = { 
            username : this.state.formControls.username.value,
            password: this.state.formControls.password.value
        };
        LoginRestClient.authenticateForToken(credentials, this.callbackLoginRequest);
    }

    }

      handleLoginSubmit()
    {
        if(this.state.formIsValid)
        {
        let username = this.state.formControls.username.value;
        let password = this.state.formControls.password.value;
        //specialconsole.log("[Login] "+username+ " " + password);
        
        let credentials  = { 
            username : this.state.formControls.username.value,
            password: this.state.formControls.password.value
        };
        LoginRestClient.authenticateForToken(credentials, this.callbackLoginRequest);
    }

    }  handleSinginSubmit()
    {
        if(this.state.formIsValid)
        {
        let username = this.state.formControls.username.value;
        let password = this.state.formControls.password.value;
        //specialconsole.log("[Login] "+username+ " " + password);
        
        let credentials  = { 
            username : this.state.formControls.username.value,
            password: this.state.formControls.password.value
        };
        LoginRestClient.insertUser(credentials, this.callbackSinginRequest);
    }

    }
    callbackLoginRequest(response, status,error)
    {
        //specialconsole.log("[callbackLoginRequest] : "+status+ ": "+response+ ": "+error );
        if(status == 200)
        {
            window.localStorage.setItem("token", response.token);
            window.localStorage.setItem("id", response.id);

            this.validateLogin();
        }
    }

 
callbackSinginRequest(response, status,error)
{
    //specialconsole.log("[callbackLoginRequest] : "+status+ ": "+response+ ": "+error );
    if(status == 200)
    {
        
        alert("User has been created");

        
    }
    else{
        alert("User could not be created");
    }
}
    validateLogin()
    {
        LoginRestClient.validateAuthentication(  this.callbackValidateLogin);

    }
    callbackValidateLogin(response, status,error)
    {
        //specialconsole.log("[callbackValidateLogin] : "+status+ ": "+response+ ": "+error );

            if(status ===200)
            {
                this.setState({redirect: true,redirectTo:"/dashboard"})
            }
    }
    render()
    {
        return(
            <div className="loginPageMainDiv">
            

            <div className="loginPageLoginFormMainDiv">
              <Form.Group>
                <Form.Label htmlFor="inputUsername">Username</Form.Label>
                <Form.Control
                    type="username"
                    id="inputUsername"
                    aria-describedby="usernameHelpBlock"
                    defaultValue={this.state.formControls.username.value}
                    touched = {this.state.formControls.username.touched?1:0}
                    valid = {this.state.formControls.username.valid?1:0}
                    name = {this.state.formControls.username.name}
                    onChange = {this.handleFormTextInputChange}

                />
                <Form.Text id="usernameHelpBlock" muted>
                {(!this.state.formControls.username.valid)&& this.state.formControls.username.validationError}
                </Form.Text>
                
                </Form.Group>

                <Form.Group>
                <Form.Label htmlFor="inputPassword">Password</Form.Label>
                <Form.Control
                    type="password"
                    id="inputPassword"
                    aria-describedby="passwordHelpBlock"
                    defaultValue={this.state.formControls.password.value}
                    touched = {this.state.formControls.password.touched?1:0}
                    valid = {this.state.formControls.password.valid?1:0}
                    name = {this.state.formControls.password.name}
                    onChange = {this.handleFormTextInputChange}
                />
                <Form.Text id="passwordHelpBlock" muted>
                    {(!this.state.formControls.password.valid)&& this.state.formControls.password.validationError}
                </Form.Text>
                
                </Form.Group>

                <div className="loginPageLoginFormButtonsDiv">
                <Button onClick ={ this.handleLoginSubmit}variant="primary" type="submit" className="loginFormButton" disabled = { this.state.formIsValid? 0:1}>
                    Log In
                </Button>
                <Button onClick = {this.handleSinginSubmit} variant="primary" type="submit" className="loginFormButton">
                    Sing Up
                </Button>
                </div>

                <hr/>

                <div className="loginPageLoginFormButtonsDiv">
                <Button
                    variant="outline-danger"
                    className="loginFormButton"
                    onClick={() => {
                        const clientId = process.env.REACT_APP_GOOGLE_CLIENT_ID;
                        const redirectUri = encodeURIComponent(window.location.origin + "/oauth2/callback");
                        const scope = encodeURIComponent("openid email profile");
                        window.location.href =
                            "https://accounts.google.com/o/oauth2/v2/auth" +
                            "?client_id=" + clientId +
                            "&redirect_uri=" + redirectUri +
                            "&response_type=code" +
                            "&scope=" + scope +
                            "&access_type=online";
                    }}
                >
                    Continue with Google
                </Button>
                </div>

                </div>
                
                {this.state.redirect && <Navigate to = {this.state.redirectTo}/>}
            </div>);
    }
}
export default LoginPage;