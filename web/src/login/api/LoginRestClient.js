import {HOST} from './../../utils/host/Hosts';
import RestApiClient from "./../../utils/http/RestApiClient";


const endpoint = {
    authenticateForToken: '/security/authenticate/getToken', 
    validateAuthentication: '/security/validate', 
    insert: "/crud/humanuser/insert"

};

 

function validateAuthentication(callback){
    let request = new Request(HOST.userManagementApi.host + HOST.userManagementApi.proxyPath+ endpoint.validateAuthentication , {
       method: 'GET'
    });

    //specialconsole.log(request.url);
    RestApiClient.performRequest(request, callback);
}

function authenticateForToken(credentials, callback){
    let request = new Request(HOST.userManagementApi.host+HOST.userManagementApi.proxyPath + endpoint.authenticateForToken , {
        method: 'POST',
        headers : {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(credentials)
    });

    //specialconsole.log("URL: " + request.url);

    RestApiClient.performRequest(request, callback);
}
function insertUser(credentials, callback){
    let request = new Request(HOST.userManagementApi.host+HOST.userManagementApi.proxyPath + endpoint.insert , {
        method: 'POST',
        headers : {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(credentials)
    });

    //specialconsole.log("URL: " + request.url);

    RestApiClient.performRequest(request, callback);
}
export {
    validateAuthentication,
    authenticateForToken,
    insertUser
  
};
