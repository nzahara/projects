import React, {Component} from 'react';
import './Register.css';
import RegisterDetails from '../Register/RegisterDetails';

class RegisterComponent extends Component {

    render() {

      const {registerObject,onSubmit} = this.props;
  return (
    // <div>
          <div id="signup">
                <input type="text" id="first" placeholder="First Name" onChange = {(evt) => {registerObject.firstName = evt.target.value}}/>
                <input type="text" id="last" placeholder="Last Name" onChange = {(evt) => {registerObject.lastName = evt.target.value}}/>
                <input type="email" id="email" placeholder="Email" onChange = {(evt) => {registerObject.emailId = evt.target.value}}/>
                <input type="password" id="password" placeholder="Password" onChange = {(evt) =>{registerObject.password = evt.target.value}}/>
                <input type="text" id="address1" placeholder="Adress 1" onChange = {(evt) =>{registerObject.addressLine1 = evt.target.value}}/>
                <input type="text" id="address2" placeholder="Adress 2" onChange = {(evt) =>{registerObject.addressLine2 = evt.target.value}}/>
                <input type="text" id="city" placeholder="City" onChange = {(evt) =>{registerObject.city = evt.target.value}}/>
                <input type="text" id="state" placeholder="State" onChange = {(evt) =>{registerObject.state = evt.target.value}}/>
                <input type="text" id="country" placeholder="Country" onChange = {(evt) =>{registerObject.country = evt.target.value}}/>
                <input type="text" id="zipcode" placeholder="zipcode" onChange = {(evt) =>{registerObject.zipCode = evt.target.value}}/>
                <input type="text" id="usertype" placeholder="usertype" onChange = {(evt) =>{registerObject.userType = evt.target.value.toUpperCase()}}/>
                <button id="send" onClick = {() => this.props.onSubmit(registerObject)}>Send</button>
            </div>
    // </div>
  );
}
}

export default RegisterComponent;
