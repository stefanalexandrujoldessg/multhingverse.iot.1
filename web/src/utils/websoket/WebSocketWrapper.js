class WebSocketWrapper
{ 
    constructor(host , path, secure, messageHandler, onOpenHandler)
    {
        this.host = host;
        this.path = path;
        this.secure = secure;
        this.webSocket =  undefined;
        this.messageHandler = messageHandler;
        this.onOpenHandler = onOpenHandler;
        this.onClose = this.onClose.bind(this);
        this.onMessage = this.onMessage.bind(this);
        this.onOpen = this.onOpen.bind(this);
        this.onError = this.onError.bind(this);
        this.initializeHeartbeat = this.initializeHeartbeat.bind(this);
        this.heartBeat = this.heartBeat.bind(this);
        this.open = this.open.bind(this);
        this.closed = false;

    }
    //ai griza la cele planificwatre ca se pot crea mai multe web socketuri spre care pierdem referinta
    open()
    {
        //specialconsole.log("[MyWebSocket] open function");
        if(!this.closed)
        {
        if(this.webSocket === undefined || this.webSocket.readyState === WebSocket.CLOSED)
        {
        if(this.webSocket!==undefined)
        {
            this.webSocket.close();
        }
        //specialconsole.log("Will try to open connection");
        let uri = "";
        if(this.secure)
        {
            uri+="wss://";
        }
        else{           
             uri+="ws://";
        }
        uri+=this.host;
        uri+=this.path;
        uri+="/";
        
        //specialconsole.log("[MyWebSocket] wil open: "+ uri)
        this.webSocket = new WebSocket(uri);
        this.webSocket.onopen = this.onOpen;
        this.webSocket.onclose = this.onClose;
        this.webSocket.onerror = this.onError;
        this.webSocket.onmessage = this.onMessage;
        //specialconsole.log("[MyWebSocket] web socket opened: "+ this.webSocket);
        return this;
    
        }
        return this;
        }
    }
    close()
    {
        clearInterval(this.interval); //daca nu faci asta aici ramane intervalul setat deci parte din obiedtul asta va raman in viata si se va executa functia dar evident ca this.ceveDinObjAsta va da undefined in heartbit
        this.webSocket.close();
        this.closed = true;
    }
    onOpen()
    {
        //specialconsole.log("Ws connected to: "+ this.webSocket);
        //this.webSocket.send("forceUpdate");
        if(this.onOpenHandler!==undefined)
        {
            this.onOpenHandler();
        }

    }
    onClose()//cateodata se apeleaza fubnctia asta can esueaza deschiderea conexiunii cateodata nu deci incearca onError
    {
        console.log("[MyWebSocket] socket closed");
        setTimeout(this.open,1000);
        
        
    }
    onError()
    {
        console.log("[MyWebSocket]: onError");
         
        setTimeout(this.open,1000);
    }
    onMessage(event)
    {
        //specialconsole.log("[MyWebSocket] message received: "+ event.data);
        if(this.messageHandler!==undefined)
        {
            this.messageHandler(event.data);
        }
        
    }
    send(data)
    {
        if( this.webSocket!== undefined && this.webSocket.readyState === WebSocket.OPEN)
        {
            this.webSocket.send(data);
            return true;
        }
        else{
            return false;
        }
    }
    initializeHeartbeat()
    {
        //specialconsole.log("Initializing heart beat");

        this.interval =setInterval(this.heartBeat, 5000);
    }
    heartBeat()
    {    //specialconsole.log("1 Sending heart beat");
        if(this.webSocket !== undefined && this.webSocket.readyState === WebSocket.OPEN)
        {        //specialconsole.log("2 Sending heart beat");

            this.webSocket.send('h');
        }
        else{
            //repopen connection this is like a sskeduled periodicaly chack method that checks if socket is close, if true then it reopens it
        }
        
        
    }



}

export default WebSocketWrapper;