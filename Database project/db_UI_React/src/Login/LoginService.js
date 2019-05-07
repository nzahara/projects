import React from 'react';
import environment from '../environments/environment';
import axios from 'axios';
import RegisterDetails from '../Register/RegisterDetails';

export default class LoginService {

  registerUserUrl = environment.JAVA_API_BASE_URL + '/register';
  loginUserUrl = environment.JAVA_API_BASE_URL + '/login';
  headers;

  constructor() {
    this.headers = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      'Access-Control-Allow-Origin': '*'
    };
  }

  registerUserData = (registerDetails: RegisterDetails) => {
    return axios.post(this.registerUserUrl, registerDetails, {
        headers: this.headers
      })
      .then(this.extractData)
      .catch(this.handleError);
  }

  loginUser = (userId: number, password: string) => {
    return axios.get(this.loginUserUrl, {
        headers: this.headers,
        params: {
          'user_name': userId,
          'password': password
        }
      })
      .then(this.extractData)
      .catch(this.handleError);
  }



  extractData(res: Response) {
    return res.data;
  }

  handleError(response: any) {
    return response.error.errors.errorMessage;
  }



}
