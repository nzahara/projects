
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { Router } from '@angular/router';
import { NotificationMessage } from './NotificationMessage';
import { RegisterDetails } from '../Register/RegisterDetails';

@Injectable({ providedIn: 'root' })
export class HomePageService {


  private searchUrl = environment.SSDI_API_URL + '/search';
  private recentPostUrl = environment.SSDI_API_URL + '/fetch/recentpost';
  private postDataUrl = environment.SSDI_API_URL + '/post/';
  private notificationUrl = environment.SSDI_API_URL + '/message';
  private unreadMessageUrl = environment.SSDI_API_URL + '/unread/messages/';
  private markMessageReadUrl = environment.SSDI_API_URL + '/mark/read';
  private deletedPostUrl = environment.SSDI_API_URL + '/delete/post';
  private updateProfileUrl = environment.SSDI_API_URL + '/update/profile';
  private headers;
  private userId: any;
  constructor(
    private http: HttpClient,
    private _cookieService: CookieService,
    private router: Router) {
    console.log("sdlksldjldjd");
    this.userId = this._cookieService.get("userId");
    console.log("sklsljsjk");
    console.log(this.userId);
    if (this.userId === undefined || this.userId == "") {
      console.log("User Id -- " + this.userId);
      let link = ['/login'];
      this.router.navigate(link);
    }
    this.headers = new HttpHeaders().append('Content-Type', 'application/json').append('Accept', 'application/json');//.append('Access-Control-Allow-Origin', '*');
  }

  getUserProfile(searchString: string) {
    return this.http.get(this.searchUrl + "/" + searchString, { headers: this.headers })
      .toPromise()
      .then(this.extractData)
      .catch(this.handleError);
  }

  fetchRecentPost(studentId: any) {
    let params = new HttpParams().append("student_id", studentId);
    return this.http.get(this.recentPostUrl, { headers: this.headers, params: params })
      .toPromise()
      .then(this.extractData)
      .catch(this.handleError);
  }

  getRecentPostByCategory(category: any) {
    return this.http.get(this.recentPostUrl + '/' + category, { headers: this.headers })
      .toPromise()
      .then(this.extractData)
      .catch(this.handleError);
  }

  postData(userId: number, category: string, message: string, file: File) {
    const formdata: FormData = new FormData();
    formdata.append("file", file);
    let headers = new HttpHeaders();
    let params = new HttpParams().append("category", category).append("message", message);
    return this.http.post(this.postDataUrl + userId, formdata, { headers: headers, params: params })
      .toPromise()
      .then(this.extractData)
      .catch(this.handleError);
  }

  sendMessage(file: File, notificationDetails: NotificationMessage) {
    const formdata = new FormData();
    formdata.append("file", file);
    console.log(formdata);
    let headers = new HttpHeaders();
    let params = new HttpParams().append("message_details", JSON.stringify(notificationDetails));
    return this.http.post(this.notificationUrl, formdata, { headers: headers, params: params })
      .toPromise()
      .then(this.extractData)
      .catch(this.handleError);
  }

  getUnReadMessages(userId: number) {
    console.log(userId);
    return this.http.get(this.unreadMessageUrl + userId, { headers: this.headers })
      .toPromise()
      .then(this.extractData)
      .catch(this.handleError);
  }

  markMessageAsRead(messageId: any) {
    let params = new HttpParams().append("message_id", messageId);
    return this.http.post(this.markMessageReadUrl, {}, { headers: this.headers, params: params })
      .toPromise()
      .then(this.extractData)
      .catch(this.handleError);
  }

  deletePost(postId: any) {
    let params = new HttpParams().append("post_id", postId);
    return this.http.post(this.deletedPostUrl, {}, { headers: this.headers, params: params })
      .toPromise()
      .then(this.extractData)
      .catch(this.handleError);
  }

  updateUserProfile(registerDetails: RegisterDetails,file:File) {
    const formdata: FormData = new FormData();
    formdata.append("file", file);
    let headers = new HttpHeaders();
    let params = new HttpParams().append("details",JSON.stringify(registerDetails));
    return this.http.post(this.updateProfileUrl, formdata, { headers: headers,params: params })
      .toPromise()
      .then(this.extractData)
      .catch(this.handleError);
  }

  private extractData(res: Response) {
    return res;
  }

  private handleError(response: any) {
    return response.error.errors[0].errorMessage;
  }

}
