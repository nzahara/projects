import { Observable } from 'rxjs';
import { Component, OnInit} from '@angular/core';
import { Router } from '@angular/router';
import { LoginService} from './Login.service';

@Component({
  selector: 'Login',
  templateUrl: './Login.component.html',
  styleUrls: ['./Login.component.scss']
})
export class LoginComponent implements OnInit {

  username: string;
  password: string;

constructor (
    private router: Router,
    private loginService : LoginService
){}

  ngOnInit () {

  }

  signIn() {
    this.loginService.login().then(
    response => {
      console.log(response);
    });
  }

  openRegisterPage() {
   let link = ['/register'];
   this.router.navigate(link);
  }
}
