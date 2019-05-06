import { Observable } from 'rxjs';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from './Login.service';
import { ToastrService } from 'ngx-toastr';
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'Login',
  templateUrl: './Login.component.html',
  styleUrls: ['./Login.component.scss']
})
export class LoginComponent implements OnInit {

  username: string = '';
  password: string = '';


  constructor(
    private router: Router,
    private loginService: LoginService,
    private toastr: ToastrService,
    private _cookieService: CookieService
  ) { }

  ngOnInit() {

  }

  signIn() {
    if (this.username == '' || this.password == '') {
      alert("Kindly enter correct details to login!");
      return;
    }
    this.loginService.login(this.username, this.password).then(
      response => {
        console.log(response);
        if (response == true) {
          this.setCookie('userId', this.username, 3);

          this.openHomePage();
          return;
        }
        alert("Unable to login!");
      });
  }


  setCookie(cname: string, cvalue: string, exdays: number) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
  }

  openHomePage() {
    let link = ['/homepage'];
    this.router.navigate(link);
  }

  openRegisterPage() {
    let link = ['/register'];
    this.router.navigate(link);
  }

}
