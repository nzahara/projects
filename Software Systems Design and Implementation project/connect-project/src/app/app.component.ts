import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { Message } from './HomePage/Message';
import { HomePageComponent } from './HomePage/HomePage.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  //title = 'UNCC Connect';

  private userId: any;
  notificationCount = 0;
  constructor(
    private router: Router,
    private _cookieService: CookieService,
    private homePageComponent: HomePageComponent
  ) { }

  ngOnInit() {
    this.userId = this._cookieService.get("userId");
    if (this.userId === undefined || this.userId == "") {
      console.log("User Id -- " + this.userId);
      let link = ['/login'];
      this.router.navigate(link);
    }
    // let link = ['/login'];
    // this.router.navigate(link);
    if (this.userId != undefined || this.userId != "") {
      console.log(this.homePageComponent.notificationCount);
      this.notificationCount = 1;
      this.homePageComponent.getUnReadMessages();
    }
  }
  // openRegisterPage() {
  //  let link = ['/register'];
  //  this.router.navigate(link);
  // }

}
