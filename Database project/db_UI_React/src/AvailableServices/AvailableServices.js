import React, {Component} from 'react';
import './AvailableServices.css';
import 'bootstrap/dist/css/bootstrap.css';

export default class AvailableServices extends Component {



  render() {
    const userId = this.props.userId;
    return (
      <div>
      <table className="table table-striped table-borderless table-hover table-card tablecss">
        <thead>
          <tr className="header">
            <th>First Name</th>
            <th>Last Name</th>
            <th>Subject Name</th>
            <th>Topic Name</th>
            <th>Zip Code</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
        {this.props.availableServices.map((value,index) => {
          return (<tr key = {index}>
            <td>{value.firstName}</td>
            <td>{value.lastName}</td>
            <td>{value.subjectName}</td>
            <td>{value.topicName}</td>
            <td>{value.zipCode}</td>
            <td><button className="btn btn-info" onClick = {() => this.props.showTutorSchedule(value,userId)}>Show Schedule</button></td>
          </tr>
        )})}
        </tbody>
        </table>
        </div>
    );
  }




}
