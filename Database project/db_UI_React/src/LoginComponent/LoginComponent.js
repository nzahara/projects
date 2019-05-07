import React from 'react';
import 'bootstrap/dist/css/bootstrap.css';
//import images from './UNCC_1.jpg';
import './LoginComponent.css';

class LoginComponent  extends React.Component {

constructor (props){
super(props);
}

state = {
  userId : 0,
  password:''
}

render() {
  return (
   <div id="login">
      <input type="text" id="email" placeholder="User Id" onChange = {(evt) => {this.setState({userId:evt.target.value})}}/>
      <input type="password" id="password" placeholder="Password" onChange = {(evt) => {this.setState({password:evt.target.value})}}/>
      <button id="send" onClick = {this.props.onLogin.bind(this,this.state.userId,this.state.password)}>Send</button>
</div>
  );
}

}


export default LoginComponent;
