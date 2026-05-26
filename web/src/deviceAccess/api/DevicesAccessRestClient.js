import {HOST} from './../../utils/host/Hosts';
import RestApiClient from "./../../utils/http/RestApiClient";


const endpoint = {
    getAdminDevicesByToken: '/crud/user/getAdminDevicesByToken', 
    getAccessDevicesByToken: '/crud/user/getAccessDevicesByToken', 
    getDeviceAccessUsers: '/crud/device/getAccessUsers', 
    addAccessUserByUsername: '/crud/device/addAccessUserByUsername',     
    removeAccessUserByUsername: '/crud/device/removeAccessUserByUsername', 
    postUserDeviceConfiguration: '/crud/user/postDeviceConfiguration',
    deleteDevice: '/crud/device/delete'

};

 

function getAdminDevices(callback){
    let request = new Request(HOST.userManagementApi.host + HOST.userManagementApi.proxyPath+ endpoint.getAdminDevicesByToken , {
       method: 'GET'
    });

    //specialconsole.log(request.url);
    RestApiClient.performRequest(request, callback);
}
 
function getAccessDevices(callback){
    let request = new Request(HOST.userManagementApi.host + HOST.userManagementApi.proxyPath+ endpoint.getAccessDevicesByToken , {
       method: 'GET'
    });

    //specialconsole.log(request.url);
    RestApiClient.performRequest(request, callback);
}
 
function getDeviceAccessUsers(deviceId, callback){
    let request = new Request(HOST.userManagementApi.host + HOST.userManagementApi.proxyPath+ endpoint.getDeviceAccessUsers +"/"+deviceId.toString(), {
       method: 'GET'
    });

    //specialconsole.log(request.url);
    RestApiClient.performRequest(request, callback);
}
function removeByUsername(deviceId,username, callback){
    let request = new Request(HOST.userManagementApi.host + HOST.userManagementApi.proxyPath+ endpoint.removeAccessUserByUsername +"/"+deviceId.toString()+"/"+username.toString(), {
       method: 'GET'
    });

    //specialconsole.log(request.url);
    RestApiClient.performRequest(request, callback);
}
function addByUsername(deviceId,username,  callback){
    let request = new Request(HOST.userManagementApi.host + HOST.userManagementApi.proxyPath+ endpoint.addAccessUserByUsername +"/"+deviceId.toString()+"/"+username.toString(), {
       method: 'GET'
    });

    //specialconsole.log(request.url);
    RestApiClient.performRequest(request, callback);
}
function deleteDevice(deviceId,  callback){
    let request = new Request(HOST.userManagementApi.host + HOST.userManagementApi.proxyPath+ endpoint.deleteDevice +"/"+deviceId.toString(), {
       method: 'GET'
    });

    //specialconsole.log(request.url);
    RestApiClient.performRequest(request, callback);
}
function postUserDeviceConfiguration(userId, deviceId, configurationJSON, callback){
    let request = new Request(HOST.userManagementApi.host+HOST.userManagementApi.proxyPath + endpoint.postUserDeviceConfiguration , {
        method: 'POST',
        headers : {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({userId:userId, deviceId:deviceId, configurationJSON:configurationJSON})
    });

    //specialconsole.log("URL: " + request.url);

    RestApiClient.performRequest(request, callback);
}
export {
    getAdminDevices,
    getDeviceAccessUsers,
    addByUsername,
    removeByUsername,
    postUserDeviceConfiguration,
    deleteDevice,
    getAccessDevices
  
};
