import React, {
  Component
} from 'react';
import 'bootstrap/dist/css/bootstrap.css';
import './StudentLogin.css';
//import StudentLoginService from './StudentLoginService';
import {
  getAvailableServices,
  fetchTutorSchedule,
  registerForService,
  payForService,
  tutorRegister
} from './StudentLoginService';
import AvailableServicesDetails from './AvailableServicesDetails';
import AvailableServices from '../AvailableServices/AvailableServices';
import TutorSchedule from '../TutorSchedule/TutorSchedule';
import TutorServiceDetails from './TutorServiceDetails';
import {
  Link
} from 'react-router-dom';
import {
  Redirect
} from 'react-router-dom';
var sessionstorage = require('sessionstorage');

export default class StudentLogin extends Component {

  state = {
    searchElement: '',
    availableServices: [],
    showAvailableServices: false,
    showTutorSchedule: false,
    showPaybtn: true,
    tutorScheduleDetails: [],
    serviceId: -1,
    registerFlag: true,
    tutorServiceDetails: [],
    showTutorService: false,
    userId: 0,
    userType: ''
  }

  componentDidMount = () => {
    console.log("sdldjdjeidj");
    console.log(this.props.location.state === undefined);
    if ((this.props.location.state === undefined) != true) {
      this.setState({
        userId: this.props.location.state.userId,
        userType: this.props.location.state.userType
      })
    }
  }

  //studentLoginService = new StudentLoginService();

  searchAvailableServices = (event) => {
    console.log(event.target.value);
    this.setState({
      searchElement: event.target.value,
      showTutorSchedule: false,
      showAvailableServices: false
    });
  }

  getAvailableServices = () => {
    getAvailableServices(this.state.searchElement)
      .then(response => {
        this.setState({
          availableServices: response,
          showAvailableServices: true,
          showTutorSchedule: false
        });
      });
  }

  getTutorSchedule = (availableService, userId) => {
    console.log(userId);
    fetchTutorSchedule(availableService.tutorAvailabilityId, userId)
      .then(response => {
        this.setState({
          tutorScheduleDetails: response,
          showAvailableServices: false,
          serviceId: availableService.serviceId,
          showTutorSchedule: true,
          showPaybtn: (response[0].registerFlag == 0 || response[0].registerFlag == 2) ? true : false,
          registerFlag: (response[0].registerFlag == 1 || response[0].registerFlag == 2) ? true : false
        });
      });
  }

  registerForService = (userId) => {
    registerForService(userId, this.state.serviceId)
      .then(response => {
        if (response) {
          alert("Successfully registered!");
          this.setState({
            showPaybtn: false,
            registerFlag: true
          })
          return;
        }
      });
  }

  payForService = (userId) => {
    payForService(userId, this.state.serviceId)
      .then(response => {
        if (response) {
          alert("Successfully paid for the service!");
          this.setState({
            showPaybtn: true
          })
          return;
        }
      });
  }

  tutorRegister = (userId) => {
    tutorRegister(userId)
      .then(response => {
        console.log(response);
        if (response) {
          alert("Successfully registered as a Tutor!");
          sessionstorage.setItem('userType', "B");
          this.setState({
            userType: "B"
          });
          return;
        }
      });
  }

  render() {
  //  console.log(this.props.location.state.userId);
  //console.log(this.state);
//   if(this.state.showTutorService === true){
//     return(
//   <Redirect to = {{pathname : '/tutor', state : {...this.state}}}/>
// );
// }
    var userId = sessionstorage.getItem('userId');
    var userType = sessionstorage.getItem('userType');
    console.log(sessionstorage.getItem('userType'));
    console.log(sessionstorage.getItem('userId'));
    return (
      <div id ="test">
      <h4 id ="title">Student Dashboard</h4>
      <div id ="tutorid">
         <Link to = "/tutor"><button className="btn btn-info" id = "btn" id = "tutorid"  disabled = {userType == "S"}>Show Tutor Dashboard</button></Link>
      </div>
    <div className="input-group" id ="search">
      <input className="form-control form-control-lg  width100" placeholder ="Search using zipcode or subject name"
          onChange = {this.searchAvailableServices} />
      <span className="input-group-btn" >
      <button className="btn btn-info" id = "btn" onClick = {this.getAvailableServices}>Search</button>
      <button className="btn btn-info" id = "btn" onClick = {this.tutorRegister.bind(this,userId)} disabled = {userType == "T" || userType == "B"}>Tutor Register</button>
      </span>
        <Link to = '/'><button className="btn btn-info" id = "logout" >Logout</button></Link>
      </div>
      <div id = "details">
      {this.state.showAvailableServices ? <AvailableServices availableServices = {this.state.availableServices} showTutorSchedule = {this.getTutorSchedule} userId = {userId}/> : null}
      {this.state.showTutorSchedule  ? <TutorSchedule tutorScheduleDetails = {this.state.tutorScheduleDetails} serviceId = {this.state.serviceId} register = {this.registerForService}
         userId = {userId}  pay = {this.payForService} paidFlag = {this.state.showPaybtn} registerFlag = {this.state.registerFlag}/> : null }
      </div>
      </div>
    );
  }

}
