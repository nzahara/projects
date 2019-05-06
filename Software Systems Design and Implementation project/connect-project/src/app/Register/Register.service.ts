
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { RegisterDetails } from './RegisterDetails';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class RegisterService {

  private registerDetailsUrl = environment.SSDI_API_URL + "/register";

  private headers;
  constructor(
    private http: HttpClient) {
    this.headers = new HttpHeaders().append('Content-Type', 'application/json').append('Accept', 'application/json');//.append('Access-Control-Allow-Origin', '*');
  }


  registerDetails(registerDetails: RegisterDetails, file: File) {
    const formdata: FormData = new FormData();
    formdata.append("file", file);
    let headers = new HttpHeaders();
    let params = new HttpParams().append("details",JSON.stringify(registerDetails));
    return this.http.post(this.registerDetailsUrl, formdata, { headers: headers,params:params})
      .toPromise()
      .then(this.extractData)
      .catch(this.handleError);
  }

  private extractData(res: Response) {
    return res;
  }

  private handleError(response: any) {
    return response.error.errors[0].defaultMessage;
  }

}
