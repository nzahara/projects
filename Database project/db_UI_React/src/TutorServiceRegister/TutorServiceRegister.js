import {
  registerTutorService
} from '../StudentLogin/StudentLoginService';
import React, {
  Component
} from 'react';
import './TutorServiceRegister.css';
import 'bootstrap/dist/css/bootstrap.css';
import DateTimePicker from 'react-datetime-picker';
var dateFormat = require('dateformat');

// var moment = require('moment');

export default class TutorServiceRegister extends Component {

state = {
  subjectList : [],
  topicList : [],
  value : 'Select Category',
  fromDate: new Date(),
  toDate : new Date(),
  topicId: 0,
  serviceType : 0,
  classSize : 0
}

filterSubjectList = (event) =>{
  console.log(event.target.value);
  let filteredList  = [];
  this.props.tutorRegisterDetails.filter(function(list) {
    if(list.categoryId == event.target.value) {
      filteredList.push({subjectId:list.subjectId,subjectName:list.subjectName});
    }
  });

 filteredList = Array.from(new Set(filteredList.map(a => a.subjectId)))
 .map(subjectId => {
   console.log(subjectId);
   return filteredList.find(a => a.subjectId === subjectId)
 })
  this.setState({
    subjectList : filteredList
  })
}

filterTopicList = (event) => {
  console.log(event.target.value);
  let filteredList  = [];
  this.props.tutorRegisterDetails.filter(function(list) {
    if(list.subjectId == event.target.value) {
      filteredList.push({topicId:list.topicId,topicName:list.topicName});
    }
  });
console.log(filteredList);
  this.setState({
    topicList : filteredList
  })
}

registerService =() =>{
  var days = ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'];
  let weekDay = days[this.state.fromDate.getDay()];
  this.state.fromDate=   dateFormat(this.state.fromDate, "yyyy-mm-dd HH:MM:ss");
  this.state.toDate = dateFormat(this.state.toDate, "yyyy-mm-dd HH:MM:ss");
console.log(this.props.userId);
console.log(this.state.fromDate > this.state.toDate);
if(this.state.fromDate > this.state.toDate) {
  alert("From Date cannot be greater than to date!");
  return;
}
registerTutorService(this.props.userId,this.state.topicId,this.state.classSize,weekDay,this.state.toDate,this.state.fromDate,this.state.serviceType)
.then(response => {
  console.log(response);
  if(response) {
    alert("Successfully Regsitered!")
    return;
  }
  alert("Unable to register!");
  return;
});
}

setToDate = (event) => {
  console.log(event);
}

  render() {
  //  console.log(this.props.tutorRegisterDetails);
  const registerDetails = this.props.tutorRegisterDetails;
    return (
      // <React.Fragment>
      <table className="taxble table-striped table-borderless table-hover table-card">
        <thead>
          <tr className="header">
            <th>Category</th>
            <th>Subject</th>
            <th>Topic</th>
            <th>Service Type</th>
            <th>Class Size</th>
            <th>From Time</th>
            <th>To Time</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
            <tr>
             <td>
             <select onClick = {this.filterSubjectList.bind(this)}>
             {this.props.categoryList.map((value) =>{
               return(
                 <option key ={value.categoryId} value = {value.categoryId}>{value.categoryType}</option>
               )})}
               </select>
            </td>
              <td>
              <select onClick = {this.filterTopicList.bind(this)}>
              {this.state.subjectList.map((value) =>{
                return(
                  <option key ={value.subjectId} value = {value.subjectId}>{value.subjectName}</option>
                )})}
            </select></td>
            <td><select onClick = {(evt) => {this.state.topicId = evt.target.value}}>
            {this.state.topicList.map((value) =>{
              return(
                <option key ={value.topicId} value ={value.topicId}>{value.topicName}</option>
              )})}
            </select></td>
              <td><select onClick = {(evt) => {this.state.serviceType = evt.target.value}}>
                <option value = "1">Online</option>
                <option value = "2">Offline</option>
                <option value = "3">Group Study</option>
              </select></td>
              <td><input type = "number" onChange = {(evt) => {this.state.classSize = evt.target.value}}/></td>
            <td>
            < DateTimePicker onChange = {fromDate => this.setState({fromDate})}  value  = {this.state.fromDate}/>
            </td>
            <td>
            < DateTimePicker onChange = {toDate => this.setState({toDate})}  value  = {this.state.toDate} />
            </td>
            <td>
            <button className="btn btn-info" onClick = {this.registerService}>Register</button>
            </td>
          </tr>
         </tbody>
        </table>
      // </React.Fragment>
    );
  }

}
