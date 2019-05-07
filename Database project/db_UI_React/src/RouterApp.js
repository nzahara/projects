import React , {Component} from 'react';
import {BrowserRouter,Route} from 'react-router-dom';
import Login from './Login/Login';
import StudentLogin from './StudentLogin/StudentLogin';
import TutorServices from './TutorServices/TutorServices';

export  default  class RouterApp extends Component{

render() {
return (
    <BrowserRouter>
        <div>
          <Route exact path = {'/'} component = {() => <Login/>} />
          <Route exact path = {'/student'} render = {(props) => <StudentLogin {...props}/>} />
          <Route exact path = {'/tutor'} render = {(props) => <TutorServices {...props}/>} />
        </div>
    </BrowserRouter>
);
}

}
