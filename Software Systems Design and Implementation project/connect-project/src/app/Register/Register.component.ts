import { Observable } from 'rxjs';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
//import { ToastrService } from 'ngx-toastr';
import { RegisterDetails } from './RegisterDetails';
import { RegisterService } from './Register.service';

@Component({
  selector: 'Register',
  templateUrl: './Register.component.html',
  styleUrls: ['./Register.component.scss']
})
export class RegisterComponent implements OnInit {

  registerDetails: RegisterDetails = new RegisterDetails();
  file: File;
  constructor(
    //    private toastr: ToastrService,
    private router: Router,
    private registerService: RegisterService
  ) { }

  ngOnInit() {

  }

isEmpty(obj) {
    for(var key in obj) {
        if(obj.hasOwnProperty(key))
            return false;
    }
    return true;
}

  registerStudent() {
    console.log(this.registerDetails)
    if (this.isEmpty(this.registerDetails) || this.isEmpty(this.registerDetails.first_name) ||this.isEmpty(this.registerDetails.last_name) || this.isEmpty(this.registerDetails.linked_in_id) || this.isEmpty( this.registerDetails.graduation_year) || this.isEmpty(this.registerDetails.contact_number) || this.isEmpty(this.registerDetails.password) ||
    this.isEmpty(this.registerDetails.major) || this.isEmpty(this.registerDetails.student_type) ||
    this.registerDetails.first_name == '' || this.registerDetails.last_name == '' || this.registerDetails.student_id  == 0 ||
                  this.registerDetails.contact_number == 0 || this.registerDetails.password == '' || this.registerDetails.linked_in_id == '' || this.registerDetails.graduation_year == 0|| this.registerDetails.major == ''|| this.registerDetails.student_type == '' || this.file == null){
                    alert("Kindly fill in all the details!");
                    return;
                  }
    if (this.registerDetails.personal_email_id == '') {
      alert("Kindly enter valid email id!");
      return;
    }
    var emailId = this.registerDetails.personal_email_id;
    var tmp = emailId.split("@");
    if (tmp.pop().toLowerCase().includes("uncc")) {
      alert("Kindly enter personal email id!");
      return;
    }
    console.log(this.file);
    this.registerService.registerDetails(this.registerDetails, this.file).then(
      response => {
        if (typeof response == 'string') {
          alert(response);
          return;
        }
        if (response == -1) {
          alert("Kindly fill in all the details!");
          return;
        }
        if (response == -2) {
          alert("Student id is already registered !Kindly login!");
          return;
        }
        alert("Successfully Registered! User Id :" + this.registerDetails.student_id);
        this.openLogin();
      }
    );
  }

  openLogin() {
    let link = ['/login'];
    this.router.navigate(link);
  }
  importFile(event: any) {
    if (event.target.files.length == 0) {
      console.log("No file selected!");
      return;
    }
    this.file = event.target.files[0];
    console.log(this.file);
  }
}
