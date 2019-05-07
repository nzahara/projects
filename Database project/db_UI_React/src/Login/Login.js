import React, {
  Component
} from 'react';
import RegisterComponent from '../Register/RegisterComponent';
import LoginComponent from '../LoginComponent/LoginComponent';
import './Login.css';
import images from '../Images/UNCC_1.jpg';
import RegisterDetails from '../Register/RegisterDetails';
import LoginService from './LoginService';
import {
  Redirect
} from 'react-router-dom';
var sessionstorage = require('sessionstorage');

export default class Login extends Component {

  // getInitialState = () => {
  //   return {signup:false,login:true};
  // }

  registerDetails = new RegisterDetails();
  loginService = new LoginService();
  state = {
    signup: false,
    login: true,
    userId: 0,
    password: '',
    studentLogin: false,
    tutorLogin : false,
    userType: ''
  }

  switch = (word) => {
    var signup, login;
    if (word === "signup") {
      signup = true;
      login = false;
    } else {
      login = true;
      signup = false;
    }
    return this.setState({
      login: login,
      signup: signup
    })

  }

  registerUser = (registerDetails) => {
    console.log(registerDetails);
    this.loginService.registerUserData(registerDetails).then(
      response => {
        console.log(typeof response);
        if (typeof(response) == 'string') {
          alert(response);
          return;
        }
        if (response == -1) {
          alert("Unable to register User !");
          return;
        }
        if (response == -2) {
          alert("Email already exists!Kindly login.");
          this.setState({
            login:true,
            signup:false
          });
          return;
        }
        //  console.log(response.data);
        alert("User Id created :" + response);
        this.setState({
          login:true,
          signup:false
        });
      });
  }

  loginUser = (userId, password) => {
    this.setState({
      userId: userId,
      password: password
    })
    this.loginService.loginUser(userId, password).then(
      response => {
        console.log(response);
        sessionstorage.clear();
        sessionstorage.setItem('userId',userId);
        sessionstorage.setItem('userType',response.toString());
        if (response.toString() == "") {
          alert("User is not registered !");
          return;
        }
         if(response.toString() == "S" || response.toString() == "B") {
        alert("Successfully Logged In!");
        this.setState({
          studentLogin: true,
          userType: response.toString()
        });
        return;
         }
         if(response.toString() == "T") {
        alert("Successfully Logged In!");
        this.setState({
          tutorLogin: true,
          userType: response.toString()
        });
        return;
         }
      }
    )
  }

render() {
  if(this.state.studentLogin){
    return(
  <Redirect to = {{pathname : '/student', state : {...this.state}}}/>
)
}
if(this.state.tutorLogin){
  return(
<Redirect to = {{pathname : '/tutor', state : {...this.state}}}/>
)
}

return(
<div>
<div align ="center">
<img  width="250" alt="University Logo" src = {images} />
</div>
  <div id="buttons">
        <p id="signupButton"  onClick={this.switch.bind(null,"signup")} className={this.state.signup ? "yellow":"blue"}>Register</p>
        <p id="loginButton" onClick={this.switch.bind(null,"login")} className={this.state.login ? "yellow":"blue"}> Login</p>
        { this.state.signup?<RegisterComponent registerObject = {this.registerDetails} onSubmit = {this.registerUser} /> : null}
        {this.state.login? <LoginComponent onLogin = {this.loginUser}/> : null}
  </div>

</div>
  );
}

}
