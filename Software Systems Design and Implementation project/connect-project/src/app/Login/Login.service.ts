import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { HttpClient , HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({providedIn: 'root'})
export class LoginService {
  private loginUrl = environment.SSDI_API_URL + '/find';
  private headers;
  constructor(
    private http: HttpClient )
    {
      this.headers = new HttpHeaders().append('Content-Type', 'application/json').append('Accept', 'application/json');//.append('Access-Control-Allow-Origin', '*');
    }

  login(){
    return this.http.get(this.loginUrl, {headers:this.headers})
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
