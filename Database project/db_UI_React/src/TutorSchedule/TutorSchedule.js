import React, {
  Component
} from 'react';
import './TutorSchedule.css';
import 'bootstrap/dist/css/bootstrap.css';

export default class TutorSchedule extends Component {

  render() {
    return (
      <div>
      <table className="table table-striped table-borderless table-hover table-card tablecss">
        <thead>
          <tr className="header">
            <th>Week Day</th>
            <th>From Time</th>
            <th>To Time</th>
            <th>Service Type</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
        {this.props.tutorScheduleDetails.map((value,index) => {
          return (<tr key = {index}>
            <td>{value.weekDay}</td>
            <td>{value.fromTime}</td>
            <td>{value.toTime}</td>
            <td>{value.serviceType}</td>
            <td><button disabled = {this.props.registerFlag} className="btn btn-info" onClick = {this.props.register.bind(this,this.props.userId)}>Register</button>
            <button disabled = {this.props.paidFlag} className="btn btn-info" onClick = {this.props.pay.bind(this,this.props.userId)} id = "paybtn">Pay</button></td>
          </tr>
        )})}
        </tbody>
        </table>
      </div>
    );
  }

}
