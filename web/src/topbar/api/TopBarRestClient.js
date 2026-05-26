import {HOST} from './../../utils/host/Hosts';
import RestApiClient from "./../../utils/http/RestApiClient";


const endpoint = {
    logOut: '/security/logOut', 
 
};

 

function logOut(callback){
    let request = new Request(HOST.userManagementApi.host + HOST.userManagementApi.proxyPath+ endpoint.logOut , {
       method: 'GET'
    });

    //specialconsole.log(request.url);
    RestApiClient.performRequest(request, callback);
}
 
 
export {
    logOut,
  
  
};
