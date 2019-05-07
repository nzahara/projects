import {
  getTutorServiceDetails,
  studentRegister,
  changeServiceStatus,
  getTutorRegisterDetails,
  getCategoryList,
  registerTutorService
} from '../StudentLogin/StudentLoginService';
import TutorServiceRegister from '../TutorServiceRegister/TutorServiceRegister';
import React, {
  Component
} from 'react';
import './TutorServices.css';
//import '../TutorSchedule/TutorSchedule.css';
import {
  Link
} from 'react-router-dom';
var sessionstorage = require('sessionstorage');


export default class TutorServices extends Component {

  state = {
    tutorServiceDetails: [],
    showStudentDash: false,
    showStudentRegister: false,
    tutorRegisterDetails : [],
    showServiceRegister : false,
    categoryList : []
  }

  constructor(props) {
    super(props);
  }



  componentDidMount = () => {
    getTutorServiceDetails(sessionstorage.getItem('userId'))
      .then(response => {
        if (response !== "") {
          this.setState({
            tutorServiceDetails: response
          });
          return response;
        }
      });
      getTutorRegisterDetails()
      .then(response => {
        console.log(response);
        this.setState({
          tutorRegisterDetails : response
        });
        return response;
      });

      getCategoryList()
        .then(response => {
          this.setState({
            categoryList : response
          })
        });
  }

  studentRegister = () => {
    studentRegister(sessionstorage.getItem('userId'))
      .then(response => {
        if (response) {
          alert("Successfully registered as a Student!");
          sessionstorage.setItem('userType', 'B');
          this.setState({
            showStudentRegister: true
          });
          return;
        }
      })
  }

  changeServiceStatus = (currentStatus, serviceId) => {
    changeServiceStatus(currentStatus, serviceId)
      .then(response => {
        if(response) {
          this.componentDidMount();
          alert("Successfully changed the status!");
          return;
        }
        alert("Unable to change the status!");
      });
  }

  showServiceRegister = () => {
    this.setState(
      {showServiceRegister : true}
    );
  }

  registerTutorService = () => {
    registerTutorService();
  }

  render() {
    const tutorServiceDetails = this.state.tutorServiceDetails;
    const userType = sessionstorage.getItem('userType');
    const userId = sessionstorage.getItem('userId');
    return (
      <div>
    <Link  to = "/student">  <button className="btn btn-info" id ="studentdash" disabled = {userType == 'T'} >Student Dashboard</button></Link>
    <button className="btn btn-info" onClick = {this.studentRegister} disabled = {userType == 'B'} id = "studentreg">Student Register</button>
    <button className="btn btn-info" id = "servicereg" onClick = {this.showServiceRegister}>Service Register</button>
    <Link to = '/'><button className="btn btn-info" id = "logouttutor" >Logout</button></Link>
      <h4 id ="title">Tutor Dashboard</h4>
      {this.state.showServiceRegister? <TutorServiceRegister tutorRegisterDetails = {this.state.tutorRegisterDetails} categoryList = {this.state.categoryList} userId = {Number(sessionstorage.getItem('userId'))}/> :
      <table className="table table-striped table-borderless table-hover table-card" id ="schedule">
        <thead>
          <tr className="header">
            <th>Subject Name</th>
            <th>Topic Name</th>
            <th>Category</th>
            <th>Service Type</th>
            <th>Class Size</th>
            <th>From Time</th>
            <th>To Time</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
        {tutorServiceDetails.map((value,index) => {
          return (<tr key = {index}>
            <td id = "tdcss">{value.subjectName}</td>
            <td>{value.topicName}</td>
            <td>{value.categoryType}</td>
            <td>{value.serviceType}</td>
            <td>{value.classSize}</td>
            <td>{value.fromTime}</td>
            <td>{value.toTime}</td>
            <td><div id = "inline"><button id = "btncss0" className="btn btn-info" disabled = {value.serviceStatus == 0 || value.serviceStatus == 1} onClick = {this.changeServiceStatus.bind(this,1,value.serviceId)}>Confirm</button>
            <button className="btn btn-info"  id  = "btncss1" disabled = {value.serviceStatus == 0} onClick = {this.changeServiceStatus.bind(this,0,value.serviceId)}>Cancel</button>
            <button className="btn btn-info" id ="btncss" disabled = {value.serviceStatus == 2} onClick = {this.changeServiceStatus.bind(this,((value.serviceStatus == 0) ? 1 : 2),value.serviceId)}>Undo</button></div></td>
          </tr>
        )})}
        </tbody>
        </table>}
      </div>

    )
  }

}
