import environment from '../environments/environment';
import axios from 'axios';

// export default class StudentLoginService {

const availableServicesSearchUrl = environment.JAVA_API_BASE_URL + '/search';
const fetchTutorScheduleUrl = environment.JAVA_API_BASE_URL + '/fetch/tutorschedule';
const registerServiceUrl = environment.JAVA_API_BASE_URL + '/student/register/';
const payServiceUrl = environment.JAVA_API_BASE_URL + '/student/pay/';
const tutorRegisterUrl = environment.JAVA_API_BASE_URL + '/register';
const studentRegisterUrl = environment.JAVA_API_BASE_URL + '/student/register';
const tutorServiceDetailsUrl = environment.JAVA_API_BASE_URL + '/tutordetails';
const changeServiceStatusUrl = environment.JAVA_API_BASE_URL + '/change/service/status';
const tutorRegisterDetailsUrl = environment.JAVA_API_BASE_URL + '/tutor/register/details';
const categoryListUrl = environment.JAVA_API_BASE_URL + "/category";
const serviceRegisterUrl = environment.JAVA_API_BASE_URL + "/tutor/service/register";
// headers;

// constructor() {
const headers = {
  'Content-Type': 'application/json',
  'Accept': 'application/json',
  'Access-Control-Allow-Origin': '*'
};
// }


export function getAvailableServices(searchString: string) {
  return axios.get(availableServicesSearchUrl, {
      headers: headers,
      params: {
        'search_string': searchString
      }
    })
    .then(extractData)
    .catch(handleError);
}

export function fetchTutorSchedule(tutorScheduleId, userId) {
  console.log(userId);
  return axios.get(fetchTutorScheduleUrl, {
      headers: headers,
      params: {
        'tutor_schedule_id': tutorScheduleId,
        'user_id': userId
      }
    })
    .then(extractData)
    .catch(handleError);
}

export function registerForService(userId: number, serviceId: number) {
  console.log(userId);
  console.log(serviceId);
  return axios.post(registerServiceUrl + userId + "/" + serviceId, {}, {
    headers: headers
  }).
  then(extractData)
    .catch(handleError);
}

export function payForService(userId: number, serviceId: number) {
  console.log(userId);
  console.log(serviceId);
  return axios.post(payServiceUrl + userId + "/" + serviceId, {}, {
    headers: headers
  }).
  then(extractData)
    .catch(handleError);
}

export function tutorRegister(userId: number) {
  return axios.post(tutorRegisterUrl + "/" + userId, {}, {
      headers: headers
    })
    .then(extractData)
    .catch(handleError);
}

export function studentRegister(userId: number) {
  return axios.post(studentRegisterUrl + "/" + userId, {}, {
      headers: headers
    })
    .then(extractData)
    .catch(handleError);
}

export function getTutorServiceDetails(userId: number) {
  return axios.get(tutorServiceDetailsUrl + "/" + userId, {
      headers: headers
    })
    .then(extractData)
    .catch(handleError);
}

export function changeServiceStatus(status: number, serviceId: number) {
  return axios.post(changeServiceStatusUrl, {}, {
      headers: headers,
      params: {
        'service_id': serviceId,
        'status': status
      }
    })
    .then(extractData)
    .catch(handleError);
}

export function getTutorRegisterDetails() {
  return axios.get(tutorRegisterDetailsUrl, {
      headers: headers
    })
    .then(extractData)
    .catch(handleError);
}

export function getCategoryList() {
  return axios.get(categoryListUrl, {
      headers: headers
    })
    .then(extractData)
    .catch(handleError);
}

export function registerTutorService(userId, topicId, classSize, weekDay, toDate, fromDate, serviceType) {
console.log(userId + topicId + classSize + weekDay + toDate + fromDate + serviceType);
  return axios.post(serviceRegisterUrl, {}, {
      headers: headers,
      params: {
        'userId': userId,
        'topicId': topicId,
        'classSize': classSize,
        'weekDay': weekDay,
        'toDate': toDate,
        'fromDate': fromDate,
        'serviceType': serviceType
      }
    })
    .then(extractData)
    .catch(handleError);
}


function extractData(res: Response) {
  return res.data;
}

function handleError(response: any) {
  console.log(response.error);
  return response.error.errors.errorMessage;
}



// }
