import { Observable, timer } from 'rxjs';
import { Component, OnInit } from '@angular/core';
import { RegisterDetails } from '../Register/RegisterDetails';
import { HomePageService } from './HomePage.service';
import { PostDetails } from './PostDetails';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { CookieService } from 'ngx-cookie-service';
import { Router } from '@angular/router';
import { NotificationMessage } from './NotificationMessage';
import { Message } from './Message';
import { interval } from 'rxjs';
//import { timer } from 'rxjs/observable/timer';
//import {AppComponent} from '../app.component';
//import 'rxjs/add/observable/interval';


@Component({
  selector: 'HomePage',
  templateUrl: './HomePage.component.html',
  styleUrls: ['./HomePage.component.scss']
})
export class HomePageComponent implements OnInit {
  registerDetails: RegisterDetails[];
  searchString: string;
  showProfile = false;
  postDetails: PostDetails[];
  category = '';
  categoryList = ['Academic', 'Entertainment', 'Food', 'Job Opportunities'];
  message = '';
  file: File;
  showRecentPost = true;
  showUser = false;
  userDetails: RegisterDetails;
  notificationMessage = new NotificationMessage();
  notificationCount = 0;
  messageDetails: Message[];
  showDeletebtn = false;
  showUpdateProfile = false;
  showUpdatebtn = false;
  subscription :any;

  constructor(
    private homePageService: HomePageService,
    public sanitizer: DomSanitizer,
    private _cookieService: CookieService,
    private router: Router,
    //private appComponent : AppComponent
    //  public viewContainerRef: ViewContainerRef,
    //  public dialogRef: MatDialogRef<PostDialog>
  ) {

  }

  ngOnInit() {
    //this.startTimer();
    this.showRecentPost = true;
    this.getRecentPosts();
    this.getUnReadMessages();
    //  this.notificationCount = this.appComponent.notificationCount;
    // Create an Observable that will publish a value on an interval
    // const secondsCounter = interval(100);
    // // Subscribe to begin publishing values
    // secondsCounter.subscribe(n =>{
    //   console.log(`It's been ${n} seconds since subscribing!`));
    //
    // }
  }

  getUserProfile(isOwnProfile = false) {
    console.log(this.searchString);
    if(isOwnProfile == false && (this.searchString == "" || this.searchString == undefined)){
      return;
    }
    if (isOwnProfile) {
      this.searchString = this._cookieService.get('userId');
      this.showUpdatebtn = true;
    }
    this.showRecentPost = false;
    this.homePageService.getUserProfile(this.searchString).then(
      response => {
        console.log(response);
        if(response.length == 0) {
          this.showRecentPost = true;
          this.showProfile = false;
          alert("User doesn't exist!");
          return;
        }
        this.registerDetails = response;
        this.showUser = false;
        this.showProfile = true;
        this.showUpdateProfile = false;
      }
    );
    this.searchString  = '';
  }

  updateProfile() {
    console.log(this.registerDetails);
    this.showUser = false;
    this.showUpdateProfile = true;
  }

  updateUserProfile() {
    if (this.registerDetails[0].first_name == '' || this.registerDetails[0].last_name == '' || this.registerDetails[0].student_id  == 0 ||
                  this.registerDetails[0].contact_number == 0 || this.registerDetails[0].password == '' || this.registerDetails[0].linked_in_id == '' || this.registerDetails[0].graduation_year == 0|| this.registerDetails[0].major == ''|| this.registerDetails[0].student_type == ''){
                    alert("Kindly fill in all the details!");
                    return;
                  }
    this.homePageService.updateUserProfile(this.registerDetails[0],this.file)
      .then(response => {
        if (response) {
          alert("Successfully Updated!");
          return;
        }
        alert("Unable to update!");
      })
  }

  showUserProfile(registerDetails: RegisterDetails) {
    console.log(registerDetails);
    this.userDetails = registerDetails;
    if(registerDetails.student_id == Number(this._cookieService.get('userId'))){
      this.showUpdatebtn = true;
    }else{
      this.showUpdatebtn = false;
    }
    this.showProfile = false;
    this.showUser = true;
  }

  showSentUserProfile(message: Message) {
    this.userDetails = new RegisterDetails();
    this.userDetails.first_name = message.firstName;
    this.userDetails.middle_name = message.middleName;
    this.userDetails.last_name = message.lastName;
    this.userDetails.student_id = message.fromStudentId;
    this.userDetails.graduation_year = message.gradationYear;
    this.userDetails.major = message.major;
    this.userDetails.linked_in_id = message.linkedInId;
    this.userDetails.profileImage = message.profilePic;
    this.showProfile = false;
    this.showUser = true;
    this.showRecentPost = false;
  }

  getRecentPosts(isMyRecentPost: boolean = false) {
    console.log(isMyRecentPost);
    let studentId = (isMyRecentPost == false) ? 0 : Number(this._cookieService.get('userId'));
    this.homePageService.fetchRecentPost(studentId).then(
      response => {
        //console.log(response);
        if (typeof response == 'string') {
          alert("Unable to fetch posts!");
          return;
        }
        if (isMyRecentPost == true) {
          this.showRecentPost = true;
          this.showDeletebtn = true;
          this.showProfile = false;
          this.showUser = false;
        }
        this.postDetails = response;
      }
    );
    this.subscription = interval(100000).subscribe((val) => {
      console.log('called');
      this.getUnReadMessages();
    });
  }

  getRecentPostByCategory(category = '') {
    this.showDeletebtn = false;
    console.log(category);
    this.homePageService.getRecentPostByCategory(category).then(
      response => {
        if (typeof response == 'string') {
          alert("Unable to fetch posts!");
          return;
        }
        if(response.length == 0){
          alert("No post found for category:" + category);
          return;
        }
        this.showRecentPost = true;
        this.showUpdateProfile = false;
        this.showProfile = false;
        this.showUser = false;
        this.showUpdateProfile = false;
        this.postDetails = response;
      }
    );
  }

  // startTimer() {
  //   const source = timer(1000, 2000);
  //   const subscribe = source.subscribe(val => console.log(val));
  // }

  signOut() {
    this.subscription.unsubscribe();
    this._cookieService.delete("userId");
  //  this.registerDetails = []
    let link = ['/login'];
    this.router.navigate(link);

  }

  postData() {
    let userId = Number(this._cookieService.get('userId'));
    if(this.category == "" || this.message == ""){
       alert("Kindly fill in the details.");
       return;
    }
    this.homePageService.postData(userId, this.category, this.message, this.file)
      .then(response => {
        if (response) {
          alert("Successfully posted!");
          this.category = '';
          this.message = '';
          document.getElementById('post_file')['value'] = null;
		  this.getRecentPosts();
          return;
        }
        alert("unable to post!");
      });
  }

  importFile(event: any) {
    if (event.target.files.length == 0) {
      console.log("No file selected!");
      return;
    }
    this.file = event.target.files[0];
  }

  isEmpty(obj) {
      for(var key in obj) {
          if(obj.hasOwnProperty(key))
              return false;
      }
      return true;
  }

  sendMessage() {
    this.notificationMessage.fromStudentId = Number(this._cookieService.get('userId'));
    this.notificationMessage.toStudentId = Number(this.userDetails.student_id);
    console.log(this.notificationMessage);
    if(this.isEmpty(this.notificationMessage) || this.notificationMessage.fromStudentId == 0 || this.notificationMessage.toStudentId == 0
                || this.isEmpty(this.notificationMessage.message) || this.notificationMessage.message.trim().length == 0 || this.notificationMessage.message == ''){
      alert("Kindly type in the message to be sent.");
      return;
    }
    console.log(this.file);
    this.homePageService.sendMessage(this.file, this.notificationMessage)
      .then(
        response => {
          console.log(response);
          alert("Message sent!");
        }
      );
  }

  getUnReadMessages() {
    this.homePageService.getUnReadMessages(Number(this._cookieService.get('userId')))
      .then(
        response => {
          console.log(response.length);
          this.notificationCount = response.length;
          console.log(this.notificationCount);
          if (response.length == 0) {
            this.messageDetails = response;
            this.notificationCount = 0;
            return;
          }
          this.messageDetails = response;
          console.log(this.messageDetails);
        }
      );
  }

  markMessageAsRead(messageId: number) {
    this.homePageService.markMessageAsRead(messageId)
      .then(response => {
        if (response) {
          alert("Marked As Read!");
          this.getUnReadMessages();
          return;
        }
        alert("Unable to mark as Read!");
      })
  }

  deletePost(postDetail: PostDetails) {
    this.homePageService.deletePost(postDetail.postId)
      .then(response => {
        if (response) {
          alert("Successfully deleted");
          this.getRecentPosts(true);
          return;
        }
        alert("Unable to delete!");
      })
  }

  // rotateImages(){
  //   console.log("dhkdhh");
  //
  //   var x=document.getElementsByClassName("images");
  //   console.log(x[0].firstChild.style.display);
  //   for (var i = 0; i < x.length; i++) {
  //     x[i].firstChild.style.display = "none";
  //   }
  //   myIndex++;
  //   if (myIndex > x.length) {myIndex = 1}
  //   x[myIndex-1].firstChild.style.display = "block";
  //   setTimeout(this.rotateImages(), 9000);
  // }

















}
