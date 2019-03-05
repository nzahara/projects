import { Component, OnInit} from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{
  //title = 'UNCC Connect';

  constructor(
    private router: Router
  ) { }

  ngOnInit () {
    let link = ['/login'];
    this.router.navigate(link);
  }
  // openRegisterPage() {
  //  let link = ['/register'];
  //  this.router.navigate(link);
  // }

}
