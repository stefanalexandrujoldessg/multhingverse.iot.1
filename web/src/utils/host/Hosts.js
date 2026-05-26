// All API traffic flows through the nginx reverse proxy on the same
// origin, so no host or port needs to be hardcoded here.
// Proxy paths defined in infrastructure/nginx/nginx.conf.
export const HOST = {
    userManagementApi: {
        host: "",
        proxyPath: "/api/um"
    },
    humanMicroserviceApi: {
        host: window.location.host,
        proxyPath: "/api/hm",
        webSocketPath: "/websocket",
        secure: window.location.protocol === "https:"
    }
}
