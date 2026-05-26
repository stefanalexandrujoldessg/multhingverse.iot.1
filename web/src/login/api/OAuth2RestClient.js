import { HOST } from './../../utils/host/Hosts';
import RestApiClient from './../../utils/http/RestApiClient';

const endpoint = {
    googleCallback: '/security/oauth2/google'
};

function exchangeGoogleCode(payload, callback) {
    let request = new Request(HOST.userManagementApi.host + HOST.userManagementApi.proxyPath + endpoint.googleCallback, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload)
    });
    RestApiClient.performRequest(request, callback);
}

export { exchangeGoogleCode };
