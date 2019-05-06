import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class LoginService {
  private loginUrl = environment.SSDI_API_URL + '/login';
  private uploadPostUrl = environment.SSDI_API_URL + '/storage/uploadPost/';
  private headers;
  private userId: any;
  constructor(
    private http: HttpClient,
    private _cookieService: CookieService,
    private router: Router) {
    this.userId = this._cookieService.get("userId");
    if (this.userId === undefined) {
      console.log("User Id -- " + this.userId);
      let link = ['/login'];
      this.router.navigate(link);
    }
    this.headers = new HttpHeaders().append('Content-Type', 'application/json').append('Accept', 'application/json');//.append('Access-Control-Allow-Origin', '*');
  }

  login(studentId: string, password: string) {
    let params = new HttpParams().append("student_id", studentId).append("password", password);
    return this.http.get(this.loginUrl,
      {
        headers: this.headers,
        params: params
      })
      .toPromise()
      .then(this.extractData)
      .catch(this.handleError);
  }

  uploadPost(file: File, category: string, fileName: string) {
    console.log("skjsjsjs");
    const formdata: FormData = new FormData();
    formdata.append("file", file);
    let params = new HttpParams().append("file_name", fileName);
    let headers = new HttpHeaders();
    return this.http.post(this.uploadPostUrl + 12 + "/" + category, formdata,
      {
        headers: headers,
        params: params
        //  responseType: 'blob'
      });
  }

  private extractData(res: Response) {
    return res;
  }

  private handleError(response: any) {
    return response.error.errors[0].errorMessage;
  }

}
