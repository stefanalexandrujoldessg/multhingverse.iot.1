/*** 
 * This is a simple switch device 
 * ID: 55b92dc6-5558-5511-5532-5536b35b9dca
 */

#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
#include <ArduinoJson.h>
#include <EEPROM.h>
#include <ESP8266HTTPClient.h>
#include <WebSocketsClient.h>
#ifndef APSSID
#define APSSID "ESPap"
#define APPSK  "thereisnospoon"
#define SwitchPin D1
#endif



int backYardLightsPin = 16;
int middleYardLightsPin = 5;
int frontYardLightsPin = 4;
int waterPumpPin = 0;

boolean backYardLightsValue = false;
boolean middleYardLightsValue = false;
boolean frontYardLightsValue = false;
boolean waterPumpValue = false;
/* Set these to your desired credentials. */
      const char *ssid = APSSID;
      const char *password = APPSK;
      IPAddress    apIP(192, 168, 0, 1);  
      IPAddress    apMSK(255, 255, 255,0);
      
      String WIFI_SSID = "";
      String WIFI_PASS = "";

      long lastTimeOnTimeExecuted=0;
      
      volatile bool maintainWiFiControl = false;
      volatile int maintainWiFiTicks = 0;
      const int maintainWiFiTicksLimit = 1;
      
      volatile bool initializeWiFiStationControl = false;

      volatile bool configurationDeserializedValidator = false;

      volatile bool webSocketClientConnectedValidator= false;
     
      volatile bool webSocketClientSendHeartBeatControl = false;
      volatile int webSocketClientSendHeartBeatTicks = 0;
        const int  webSocketClientSendHeartBeatTicksLimit = 1;
      ESP8266WebServer httpServer(80);
      WebSocketsClient webSocketClient;


      String configurationJSON =    
"{\"id\":\"55b92dc6-5558-5511-5532-5536b35b9dca\",\"name\":\"Garden local box\",\"state\":{\"attr1\":{\"name\":\"Back yard lights\",\"metaType\":\"JSON\",\"type\":\"boolean\",\"dashboardDisplay\":true,\"value\":{\"value\":true,\"displayValue\":\"on\"}},\"attr2\":{\"name\":\"Middle yard lights\",\"metaType\":\"JSON\",\"type\":\"boolean\",\"dashboardDisplay\":true,\"value\":{\"value\":true,\"displayValue\":\"on\"}},\"attr3\":{\"name\":\"Front yard lights\",\"metaType\":\"JSON\",\"type\":\"boolean\",\"dashboardDisplay\":true,\"value\":{\"value\":true,\"displayValue\":\"on\"}},\"attr4\":{\"name\":\"Water pump\",\"metaType\":\"JSON\",\"type\":\"boolean\",\"dashboardDisplay\":true,\"value\":{\"value\":true,\"displayValue\":\"running\"}}},\"capabilities\":{\"cap1\":{\"dashboardDisplay\":true,\"name\":\"Set back yard lights\",\"parameters\":{\"param1\":{\"name\":\"\",\"metaType\":\"JSON\",\"type\":\"boolean\",\"corelationAttribute\":\"attr1\"}}},\"cap2\":{\"dashboardDisplay\":true,\"name\":\"Set middle yard lights\",\"parameters\":{\"param1\":{\"name\":\"\",\"metaType\":\"JSON\",\"type\":\"boolean\",\"corelationAttribute\":\"attr2\"}}},\"cap3\":{\"dashboardDisplay\":true,\"name\":\"Set front yard lights\",\"parameters\":{\"param1\":{\"name\":\"\",\"metaType\":\"JSON\",\"type\":\"boolean\",\"corelationAttribute\":\"attr3\"}}},\"cap4\":{\"dashboardDisplay\":true,\"name\":\"Set water pump\",\"parameters\":{\"param1\":{\"name\":\"\",\"metaType\":\"JSON\",\"type\":\"boolean\",\"corelationAttribute\":\"attr4\"}}},\"cap5\":{\"dashboardDisplay\":true,\"name\":\"Set all lights\",\"parameters\":{\"param1\":{\"name\":\"\",\"metaType\":\"JSON\",\"type\":\"boolean\"}}},\"cap6\":{\"dashboardDisplay\":true,\"name\":\"All off\"}}}"
;
          //This will be used for fast processing and message generation and function fetching
      DynamicJsonDocument configuration (4096);


 
      void printCredentials();
      void persistCredentials(String credentialsJSON);
      
    //  void ICACHE_RAM_ATTR onTime() 
        void onTime()
      {
            if(configurationDeserializedValidator ==true)
            {
                maintainWiFiTicks++;
                
                if(maintainWiFiTicks >= maintainWiFiTicksLimit)
                {
                    maintainWiFiTicks =0;
                    maintainWiFiControl = true;
                }

                 webSocketClientSendHeartBeatTicks++;
                
                if(webSocketClientSendHeartBeatTicks >= webSocketClientSendHeartBeatTicksLimit)
                {
                    webSocketClientSendHeartBeatTicks =0;
                    webSocketClientSendHeartBeatControl = true;
                }
            }
            
            //maintainWiFiConnection();// nu aici aici doar setezi o variabila si apoi in loop faci asta
            // Re-Arm the timer as using TIM_SINGLE
            //timer1_write(2500000);//12us
      }

            
      void initializeTimer()
      {
            noInterrupts();
            //Initialize Ticker every 0.5s
            timer1_disable();
          
            timer1_isr_init();
            timer1_attachInterrupt(onTime); // Add ISR Function
            timer1_isr_init();
            timer1_enable(TIM_DIV256, TIM_EDGE, TIM_LOOP);
            /* Dividers:
              TIM_DIV1 = 0,   //80MHz (80 ticks/us - 104857.588 us max)
              TIM_DIV16 = 1,  //5MHz (5 ticks/us - 1677721.4 us max)
              TIM_DIV256 = 3  //312.5Khz (1 tick = 3.2us - 26843542.4 us max)
            Reloads:
              TIM_SINGLE  0 //on interrupt routine you need to write a new value to start the timer again
              TIM_LOOP  1 //on interrupt the counter will start with the same value again
            */
            
            // Arm the Timer for our 0.5s Interval
            timer1_write(2500000); // 2500000 / 5 ticks per us from TIM_DIV16 == 500,000 us interval
            interrupts();
    }
    void handleRoot() 
    {
            printCredentials();
            httpServer.sendHeader("Access-Control-Allow-Origin","*");
            httpServer.sendHeader("Access-Control-Allow-Methods","POST, GET, OPTIONS");
            httpServer.sendHeader("Access-Control-Allow-Headers","Authorization, Content-Type"); 
            if(configurationDeserializedValidator==true)
            {
                httpServer.send(200, "text/html",     
                      "<html> <style> body>*:{border-radius:1rem; font-size:2rem;} body{font-size:2rem; display:flex; flex-direction:column; justify-content:flex-start; align-content: center; align-items:center;} </style>   <script>         function  callback(response, status, error){  console.log(response, status, error);  alert(status+\" \"+ (response!==null && response!==undefined)?response.toString():\"\" + \" \"+ (error!==null && error!==undefined)?error.toString():\"\");}function postCredentials(){    let request = new Request(\"/postCredentials\" , {        method: 'POST',        headers : {             'Content-Type': 'application/json',        },        body: JSON.stringify({password: document.getElementById('password').value, ssid: document.getElementById(\"ssid\").value}),    });         performRequestInternal(request)}function postPair(){    let request = new Request(\"/pairDevice\" , {        method: 'POST',        headers : {             'Content-Type': 'application/json',        },        body: JSON.stringify({username: document.getElementById('username').value, password: document.getElementById('password').value, ssid: document.getElementById(\"ssid\").value}),    });         performRequestInternal(request)}function getIndex(){    let request = new Request(\"/\" , {        method: 'GET',        });         performRequestInternal(request)}  function performRequestInternal(request){               fetch(request)                .then(            function(response) {                if (response.ok) {  response.text().then((text)=>callback(text, response.status,null));               }                else {     response.text().then((text)=>callback(null, response.status,text));            }            })        .catch(function (err) {                       callback(null, 1, err)        });}    </script>    <body>            <label for=\"username\">username</label>            <input type=\"text\" id = \"username\"/>            <label for=\"password\">password</label>            <input type=\"text\" id = \"password\"/>            <label for=\"ssid\">ssid</label>            <input type=\"text\" id = \"ssid\"/>                         <button onclick=\"postCredentials()\">postCredentials</button>            <button onclick=\"getIndex()\">getIndex</button>            <button onclick=\"postPair()\">pair with cloud</button>                 </body></html>"
                );    
            }
            else
            {
                httpServer.send(200, "text/html",     
                    "<h1> Configuration deserialization critical error. Contact the producer for devfice updates.</h1>"
                 );  
            }
            
    }
 
    void handleWiFiConfigurationBody() 
    {  
          
           if (httpServer.hasArg("plain")== false)
           { 
                httpServer.sendHeader("Access-Control-Allow-Origin","*");
                httpServer.sendHeader("Access-Control-Allow-Methods","POST, GET, OPTIONS");
                httpServer.sendHeader("Access-Control-Allow-Headers","Authorization, Content-Type"); 
                httpServer.send(500, "text/plain", "Body not received");
                return;
           }
           else
           {
                String body = httpServer.arg("plain");
                
                StaticJsonDocument<1024> jsonObj;
                DeserializationError error = deserializeJson(jsonObj, body);
     
                if (error) 
                {
                    httpServer.sendHeader("Access-Control-Allow-Origin","*");
                    httpServer.sendHeader("Access-Control-Allow-Methods","POST, GET, OPTIONS");
                    httpServer.sendHeader("Access-Control-Allow-Headers","Authorization, Content-Type"); 
                    httpServer.send(500, "text/plain", "Deserialization error");
                }
                else
                {  
                    String wifi_ssid= String(jsonObj["ssid"]);
                    String wifi_pass= String(jsonObj["password"]);
                    if((!WIFI_SSID.equals(wifi_ssid)) || (!WIFI_PASS.equals(wifi_pass)))
                    {
                        WIFI_SSID = wifi_ssid;
                        WIFI_PASS = wifi_pass;
                        Serial.println("Will update credentials");
                        
                        String persistance = "{\"ssid\":\""+WIFI_SSID+"\",\"password\":\"" +WIFI_PASS +"\"}";
                        persistCredentials(persistance);
                        initializeWiFiStationControl = true;
                    }
                    
                    Serial.println("Credentials "+ WIFI_SSID+ " " + WIFI_PASS);

                }
     
            
          }
    
          httpServer.sendHeader("Access-Control-Allow-Origin","*");
          httpServer.sendHeader("Access-Control-Allow-Methods","POST, GET, OPTIONS");
          httpServer.sendHeader("Access-Control-Allow-Headers","Authorization, Content-Type"); 
          httpServer.send(200, "text/plain", "ok");
        
    }

    void handlePairBody() 
    {  
         
        if (httpServer.hasArg("plain")== false)
        { 
              httpServer.sendHeader("Access-Control-Allow-Origin","*");
              httpServer.sendHeader("Access-Control-Allow-Methods","POST, GET, OPTIONS");
              httpServer.sendHeader("Access-Control-Allow-Headers","Authorization, Content-Type"); 
              httpServer.send(500, "text/plain", "No body received");
              return ;
        }
        else
        {
              String body = httpServer.arg("plain");
              Serial.println(body);
              StaticJsonDocument<1024> jsonObj;
              DeserializationError error = deserializeJson(jsonObj, body);
     
              if (error) 
              {
                  httpServer.sendHeader("Access-Control-Allow-Origin","*");
                  httpServer.sendHeader("Access-Control-Allow-Methods","POST, GET, OPTIONS");
                  httpServer.sendHeader("Access-Control-Allow-Headers","Authorization, Content-Type"); 
                  httpServer.send(500, "text/plain", "Deserialization error");
                  return ;
              }
              else
              { 
                  
                  String wifi_ssid= String(jsonObj["ssid"]);
                  String wifi_pass= String(jsonObj["password"]);
                  String username= String(jsonObj["username"]);
                  if(username.length()>0 && wifi_ssid.length()>0 && wifi_pass.length()>0 )
                  { 
                      if((!WIFI_SSID.equals(wifi_ssid)) || (!WIFI_PASS.equals(wifi_pass)))
                      {
    
                          WIFI_SSID = wifi_ssid;
                          WIFI_PASS = wifi_pass;
                          Serial.println("Will update credentials");
                         
                          String persistance = "{\"ssid\":\""+WIFI_SSID+"\",\"password\":\"" +WIFI_PASS +"\"}";
                          persistCredentials(persistance);
                           
                      }
                      Serial.println("PairBody "+ WIFI_SSID+ " " + WIFI_PASS + " " + username);

                      if(initializeWiFiStationPair() == true)
                      {
                          if(pairDeviceWithCloud(username) == true)
                          {
                               httpServer.sendHeader("Access-Control-Allow-Origin","*");
                                httpServer.sendHeader("Access-Control-Allow-Methods","POST, GET, OPTIONS");
                                httpServer.sendHeader("Access-Control-Allow-Headers","Authorization, Content-Type"); 
                                httpServer.send(200, "text/plain", "Device paired with the platform");
                          }
                          else
                          {
                               httpServer.sendHeader("Access-Control-Allow-Origin","*");
                               httpServer.sendHeader("Access-Control-Allow-Methods","POST, GET, OPTIONS");
                               httpServer.sendHeader("Access-Control-Allow-Headers","Authorization, Content-Type"); 
                               httpServer.send(500, "text/plain", "Could not pair with cloud");
                          }
                      }
                      else
                      {
                            httpServer.sendHeader("Access-Control-Allow-Origin","*");
                            httpServer.sendHeader("Access-Control-Allow-Methods","POST, GET, OPTIONS");
                            httpServer.sendHeader("Access-Control-Allow-Headers","Authorization, Content-Type"); 
                            httpServer.send(500, "text/plain", "Could not connect to WiFii");
                      }
                    
                    
                    
                  }

              }
     
            
          }
    
          httpServer.sendHeader("Access-Control-Allow-Origin","*");
          httpServer.sendHeader("Access-Control-Allow-Methods","POST, GET, OPTIONS");
          httpServer.sendHeader("Access-Control-Allow-Headers","Authorization, Content-Type"); 
          httpServer.send(200, "text/plain", "Something went wrong");
           
    }

    void maintainWiFi()
    {
        if(WiFi.status() != WL_CONNECTED)
        {
            delay(100);
            Serial.print(".");
        }
        else
        {
            Serial.println("Conected!");
        }
    }



 
    String getInsertionBodyJSONForInsertion(String username)
    {
        DynamicJsonDocument doc (4096);
        
        doc["adminUserUsername"] = username;
        doc["configurationJSON"]    = configurationJSON;
        String payload;
        serializeJson(doc, payload);
        Serial.println(payload);
        return payload;
    }

    bool pairDeviceWithCloud(String username)
    {
     
        WiFiClient client;
        HTTPClient http;
    
        Serial.print("[HTTP] begin...\n");
        if (http.begin(client, "http://iot.multhingverse.com/api/um/crud/device/insertWithUsernameByConfiguration")) 
        {   
            http.addHeader("Content-Type", "application/json");
            String body = getInsertionBodyJSONForInsertion(username);
            
            Serial.println("[HTTP] POST..."+body);
            
            int httpCode = http.POST(body);
            
            if (httpCode > 0) 
            {
                Serial.printf("[HTTP] GET... code: %d\n", httpCode);
            
                if (httpCode == HTTP_CODE_OK || httpCode == HTTP_CODE_MOVED_PERMANENTLY) 
                {
                    http.end();
                    return true;
                
                }
                else 
                {
                Serial.printf("[HTTP] GET... failed, error: %s\n", http.errorToString(httpCode).c_str());
                }
            } 
            else 
            {
                Serial.printf("[HTTP] GET... failed, error: %s\n", http.errorToString(httpCode).c_str());
            }
            
           
        }
        else 
        {
            Serial.printf("[HTTP} Unable to connect\n");
        }

        http.end();
        return false;
    
    
    }







    String generateStateUpdateMessageBool(String attrId, bool value)
    {
      DynamicJsonDocument response (2048);
      response["type"] = "deviceState";
      response["deviceState"]["id"] = configuration["id"];
      response["deviceState"]["state"][attrId]["value"]["value"] = value;
      response["deviceState"]["state"][attrId]["value"]["displayValue"] = value?"on":"off";
      String message ;
      serializeJson(response, message);
      return message;

    }
    String generateStateUpdateMessageInt(String attrId, int value)
    {
      DynamicJsonDocument response (2048);
      response["type"] = "deviceState";
      response["deviceState"]["id"] = configuration["id"];
      response["deviceState"]["state"][attrId]["value"]["value"] = value;
      String message ;
      serializeJson(response, message);
      return message;

    }
     String generateStateUpdateMessageFloat(String attrId, float value)
    {
      DynamicJsonDocument response (2048);
      response["type"] = "deviceState";
      response["deviceState"]["id"] = configuration["id"];
      response["deviceState"]["state"][attrId]["value"]["value"] = value;
      String message ;
      serializeJson(response, message);
      return message;

    }
    String generateStateUpdateMessageString(String attrId, String value)
    {
      DynamicJsonDocument response (2048);
      response["type"] = "deviceState";
      response["deviceState"]["id"] = configuration["id"];
      response["deviceState"]["state"][attrId]["value"]["value"] = value;
      String message ;
      serializeJson(response, message);
      return message;

    }
    void    initializeGPIOPins()
    {
        pinMode(backYardLightsPin, OUTPUT);
        pinMode(middleYardLightsPin, OUTPUT);
        pinMode(frontYardLightsPin, OUTPUT);
        pinMode(waterPumpPin, OUTPUT);

         
        synchGPIO();

        
    }
void    synchGPIO()
    {
       
        digitalWrite(backYardLightsPin, !backYardLightsValue);
        digitalWrite(middleYardLightsPin, !middleYardLightsValue);
        digitalWrite(frontYardLightsPin, !frontYardLightsValue);
        digitalWrite(waterPumpPin, !waterPumpValue);
       

      
    }

    void setGenericBoolValue(bool * attributeValuePointer, String attrId, bool value)
    {
        *attributeValuePointer = value;
        synchGPIO();

        String message= generateStateUpdateMessageBool(attrId, value);
        Serial.println(message);
        webSocketClient.sendTXT(message);
      
    }
     
      
 

    
    void handleRequestAuthorizationMessage(DynamicJsonDocument jsonObj)
    {
        
        if(!jsonObj["type"].isNull())
        {
            if(jsonObj["type"]=="requestAuthorization")
            {
                String response = generateAuthorizationMessage();
                webSocketClient.sendTXT(response);
                
            }
        }
        
    }

    
    void handleCapabilityMessage(DynamicJsonDocument jsonObj)
    {
        
        if(!jsonObj["type"].isNull())
        {
            if(jsonObj["type"]=="deviceCapability")
            {
                 if(!jsonObj["deviceCapability"].isNull())
                 {
                     if(!jsonObj["deviceCapability"]["capability"].isNull())
                     {
                         if(!jsonObj["deviceCapability"]["capability"]["cap1"].isNull())
                         {
                             if(!jsonObj["deviceCapability"]["capability"]["cap1"]["parameters"].isNull())
                             {
                                 if(!jsonObj["deviceCapability"]["capability"]["cap1"]["parameters"]["param1"].isNull())
                                 {
                                     if(!jsonObj["deviceCapability"]["capability"]["cap1"]["parameters"]["param1"]["value"].isNull())
                                     {
                                         if(!jsonObj["deviceCapability"]["capability"]["cap1"]["parameters"]["param1"]["value"]["value"].isNull())
                                         {
                                             if(jsonObj["deviceCapability"]["capability"]["cap1"]["parameters"]["param1"]["value"]["value"].is<bool>())
                                             {
                                                 Serial.println("Cap 1 activated");
                                                 setGenericBoolValue(&backYardLightsValue, "attr1", jsonObj["deviceCapability"]["capability"]["cap1"]["parameters"]["param1"]["value"]["value"].as<bool>());
                                             }
                                         }
                                     }
                                 }
                             }
                         }


                          if(!jsonObj["deviceCapability"]["capability"]["cap2"].isNull())
                         {
                             if(!jsonObj["deviceCapability"]["capability"]["cap2"]["parameters"].isNull())
                             {
                                 if(!jsonObj["deviceCapability"]["capability"]["cap2"]["parameters"]["param1"].isNull())
                                 {
                                     if(!jsonObj["deviceCapability"]["capability"]["cap2"]["parameters"]["param1"]["value"].isNull())
                                     {
                                         if(!jsonObj["deviceCapability"]["capability"]["cap2"]["parameters"]["param1"]["value"]["value"].isNull())
                                         {
                                             if(jsonObj["deviceCapability"]["capability"]["cap2"]["parameters"]["param1"]["value"]["value"].is<bool>())
                                             {
                                                 Serial.println("Cap 2 activated");
                                                 setGenericBoolValue(&middleYardLightsValue, "attr2",  jsonObj["deviceCapability"]["capability"]["cap2"]["parameters"]["param1"]["value"]["value"].as<bool>());
                                             }
                                         }
                                     }
                                 }
                             }
                         }

                         if(!jsonObj["deviceCapability"]["capability"]["cap3"].isNull())
                         {
                             if(!jsonObj["deviceCapability"]["capability"]["cap3"]["parameters"].isNull())
                             {
                                 if(!jsonObj["deviceCapability"]["capability"]["cap3"]["parameters"]["param1"].isNull())
                                 {
                                     if(!jsonObj["deviceCapability"]["capability"]["cap3"]["parameters"]["param1"]["value"].isNull())
                                     {
                                         if(!jsonObj["deviceCapability"]["capability"]["cap3"]["parameters"]["param1"]["value"]["value"].isNull())
                                         {
                                             if(jsonObj["deviceCapability"]["capability"]["cap3"]["parameters"]["param1"]["value"]["value"].is<bool>())
                                             {
                                                 Serial.println("Cap 3 activated");
                                                 setGenericBoolValue(&frontYardLightsValue, "attr3",  jsonObj["deviceCapability"]["capability"]["cap3"]["parameters"]["param1"]["value"]["value"].as<bool>());
                                             }
                                         }
                                     }
                                 }
                             }
                         }

                         if(!jsonObj["deviceCapability"]["capability"]["cap4"].isNull())
                         {
                             if(!jsonObj["deviceCapability"]["capability"]["cap4"]["parameters"].isNull())
                             {
                                 if(!jsonObj["deviceCapability"]["capability"]["cap4"]["parameters"]["param1"].isNull())
                                 {
                                     if(!jsonObj["deviceCapability"]["capability"]["cap4"]["parameters"]["param1"]["value"].isNull())
                                     {
                                         if(!jsonObj["deviceCapability"]["capability"]["cap4"]["parameters"]["param1"]["value"]["value"].isNull())
                                         {
                                             if(jsonObj["deviceCapability"]["capability"]["cap4"]["parameters"]["param1"]["value"]["value"].is<bool>())
                                             {
                                                 Serial.println("Cap 4 activated");
                                                 setGenericBoolValue(&waterPumpValue, "attr4",  jsonObj["deviceCapability"]["capability"]["cap4"]["parameters"]["param1"]["value"]["value"].as<bool>());
                                             }
                                         }
                                     }
                                 }
                             }
                         }

                         if(!jsonObj["deviceCapability"]["capability"]["cap5"].isNull())
                         {
                             if(!jsonObj["deviceCapability"]["capability"]["cap5"]["parameters"].isNull())
                             {
                                 if(!jsonObj["deviceCapability"]["capability"]["cap5"]["parameters"]["param1"].isNull())
                                 {
                                     if(!jsonObj["deviceCapability"]["capability"]["cap5"]["parameters"]["param1"]["value"].isNull())
                                     {
                                         if(!jsonObj["deviceCapability"]["capability"]["cap5"]["parameters"]["param1"]["value"]["value"].isNull())
                                         {
                                             if(jsonObj["deviceCapability"]["capability"]["cap5"]["parameters"]["param1"]["value"]["value"].is<bool>())
                                             {
                                                 Serial.println("Cap 5 activated");
                                                 setGenericBoolValue(&frontYardLightsValue, "attr1",  jsonObj["deviceCapability"]["capability"]["cap5"]["parameters"]["param1"]["value"]["value"].as<bool>());
                                                 setGenericBoolValue(&middleYardLightsValue, "attr2",  jsonObj["deviceCapability"]["capability"]["cap5"]["parameters"]["param1"]["value"]["value"].as<bool>());
                                                 setGenericBoolValue(&backYardLightsValue, "attr3",  jsonObj["deviceCapability"]["capability"]["cap5"]["parameters"]["param1"]["value"]["value"].as<bool>());
                                             }
                                         }
                                     }
                                 }
                             }
                         }

                          if(!jsonObj["deviceCapability"]["capability"]["cap6"].isNull())
                         {
                              
                                                 Serial.println("Cap 6 activated");
                                                 setGenericBoolValue(&frontYardLightsValue, "attr1", false);
                                                 setGenericBoolValue(&middleYardLightsValue, "attr2",  false);
                                                 setGenericBoolValue(&backYardLightsValue, "attr3",  false);
                                                 setGenericBoolValue(&waterPumpValue, "attr4",  false);
                                                 
                                       
                                           
                  
                         }

                           

                         
                         
                         
                     }
                 }
                
            }
        }
        
    }
    void textMessageHandlerChain(String message)
    {
        DynamicJsonDocument  jsonObj(2048);
        DeserializationError error = deserializeJson(jsonObj, message);
          
        if (error) 
        {
            Serial.println(" [handleRequestAuthorizationMessage]: deserializeJson() failed: ");
            Serial.println(error.f_str());
            return;
        }
        handleRequestAuthorizationMessage(  jsonObj);
        handleCapabilityMessage(jsonObj);
    }
    String generateAuthorizationMessage()
    {
        DynamicJsonDocument  message(1024);
        message["type"] ="authorization";
        message["authorization"] = String("Id ")+String(configuration["id"]);
        String response;
        serializeJson(message, response);
        return response;
    
    }
    void webSocketEvent(WStype_t type, uint8_t * payload, size_t length) 
    {
         Serial.println("WS event");
        switch(type) 
        {
            case WStype_DISCONNECTED:
            {
                Serial.printf("[WSc] Disconnected!\n");
                break;
            }
            
            case WStype_CONNECTED: 
            {
                Serial.printf("[WSc] Connected to url: %s\n", payload);
                webSocketClient.sendTXT("Connected");
                
                String messsage = generateAuthorizationMessage();
               // webSocketClient.sendTXT(messsage);
                Serial.println(messsage);
                break;
            }
            
            case WStype_TEXT:
            {
                Serial.printf("[WSc] get text: %s\n", payload);
                Serial.flush();
                textMessageHandlerChain(  String((char *)payload));
                break;
            }
            
            
            case WStype_BIN:
            {
                Serial.printf("[WSc] get binary length: %u\n", length);
                for (size_t i = 0; i < length; i++) {
                    Serial.printf("%02X ", payload[i]);
                }
                Serial.println();
                break;
            }
            
            case WStype_PING:
            {
                     // pong will be send automatically
                Serial.printf("[WSc] get ping\n");
                break;
            }
            case WStype_PONG:
            {
                    // answer to a ping we send
                Serial.printf("[WSc] get pong\n");
                break;
            }
        }
    
    }

    void initializeWebSocketClient()
    {
        Serial.println("WS binitialize");
        webSocketClient.begin("iot.multhingverse.com", 80, "/api/dm/websocket");
        webSocketClient.onEvent(webSocketEvent);
        webSocketClient.setReconnectInterval(1000);
    }
    
    void sendHeartBeat()
    {
        webSocketClient.sendTXT("h");
    }
    
    
    


  bool initializeWiFiStationPair(  )
  {
             if(WIFI_SSID.length()>0 && WIFI_PASS.length()>0 )
            {
                 WiFi.begin(WIFI_SSID, WIFI_PASS);

                // Connecting to WiFi...
                Serial.print("Connecting to ");
                Serial.print(WIFI_SSID);
                int numTry = 0;
                while (WiFi.status() != WL_CONNECTED && numTry<250)
                {
                  delay(100);
                  Serial.print(".");
                  numTry++;
                }
                if(WiFi.status() == WL_CONNECTED)
                {
                
                    Serial.println();
                    Serial.println("Connected!");
                    Serial.print("IP address for network ");
                    Serial.print(WIFI_SSID);
                    Serial.print(" : ");
                    Serial.println(WiFi.localIP());
                    return true;
                }
                else
                {
                  Serial.println("Could not connect to "+ WIFI_SSID);
                }
            }
            return false;
  }

  
  void initializeWiFiStation(  )
  {
             if(WIFI_SSID.length()>0 && WIFI_PASS.length()>0 )
            {
                 WiFi.begin(WIFI_SSID, WIFI_PASS);

                // Connecting to WiFi...
                Serial.print("Connecting to ");
                Serial.print(WIFI_SSID);
                int numTry = 0;
                while (WiFi.status() != WL_CONNECTED && numTry<50)
                {
                  delay(100);
                  Serial.print(".");
                  numTry++;
                }
                if(WiFi.status() == WL_CONNECTED)
                {
                
                    Serial.println();
                    Serial.println("Connected!");
                    Serial.print("IP address for network ");
                    Serial.print(WIFI_SSID);
                    Serial.print(" : ");
                    Serial.println(WiFi.localIP());
                    //initializeWebSocketClient();
                }
                else
                {
                  Serial.println("Could not connect to "+ WIFI_SSID);
                }
            }
  }
  
  void  initializeWiFiAp()
    {
            Serial.print("Configuring access point...");
            /* You can remove the password parameter if you want the AP to be open. */
            WiFi.mode(WIFI_AP_STA);
            WiFi.softAPConfig(apIP,apIP,apMSK );
             
             if(configurationDeserializedValidator==true)
            {
              char a[50] ;
              String sid =   String(configuration["id"]);
              sid.toCharArray(a,50);
              Serial.println("WiFi AP ssid");
              Serial.println(a);
              
              WiFi.softAP(a+ 24);//String(configuration["id"].toCharArray());
            }
            else
            {
              WiFi.softAP(ssid);
            }
          
           
            Serial.print("IP address for network ");
            Serial.print(APSSID);
            Serial.print(" : ");
            Serial.print(WiFi.softAPIP());
    }



    
   void initializeHttpServer()
    {
            httpServer.on("/", handleRoot);
            httpServer.on("/postCredentials",  handleWiFiConfigurationBody);
            httpServer.on("/pairDevice",  handlePairBody);
            httpServer.begin();
            Serial.println("HTTP server started");
    }

    void   initializeEEPROM()
    {
      EEPROM.begin(1024);
     
      
    }
    void   persistCredentials(String credentialsJSON)
    {
      
      int le = credentialsJSON.length();
      char * buf = (char *) malloc((le+1) * sizeof(char));
      if(buf == NULL)
      {
        Serial.println("Could not allocate memory");\
        return;
      }
     
      credentialsJSON.toCharArray(buf, le+1);
      buf[le]='\0';
       //Serial.println(buf);
      EEPROM.put(0, true);
      EEPROM.put(4,le);
      int address = 8;
      for (int  i = 0 ; i<le;i++)
      {
        EEPROM.write(address, buf[i]);
        address=address+sizeof(buf[i]);
        Serial.println(address);
      }
      EEPROM.write(address, '\0');

      EEPROM.commit();
      free(buf);
    }

    
    void   printCredentials( )
    {
      bool valid ;
      int le;
      String credentialsJSON; 
      EEPROM.get(0, valid);     
      EEPROM.get(4, le);
      if(valid == true)
      {
        if(le>0 && le <100)
        {
          char * buf = (char *) malloc((le+1) * sizeof(char));
          if(buf == NULL)
          {
            Serial.println("Allocation failed");
            return ;
          }
          int address = 8;
          for (int i = 0 ; i< le ; i++)
          {
            buf[i] = EEPROM.read(address);
            address= address+sizeof(buf[i]);
           // Serial.println(address);
          }
          buf[le] = '\0';
          Serial.println(valid);
          Serial.println(le);
          Serial.println(buf);
          free(buf);
        }
      }
    
      
    }

    void   retreiveCredentials( )
    {
      bool valid ;
      int le;
      String credentialsJSON; 
      EEPROM.get(0, valid);     
      EEPROM.get(4, le);
      if(valid == true)
      {
        if(le>0 && le <100)
        {
          char * buf = (char *) malloc((le+1) * sizeof(char));
          if(buf == NULL)
          {
            Serial.println("Allocation failed");
            return ;
          }
          int address = 8;
          for (int i = 0 ; i< le ; i++)
          {
            buf[i] = EEPROM.read(address);
            address= address+sizeof(buf[i]);
            //Serial.println(address);
          }
          buf[le] = '\0';
          Serial.println(valid);
          Serial.println(le);
          Serial.println(buf);
          
          StaticJsonDocument<1024> jsonObj;
          DeserializationError error = deserializeJson(jsonObj, String(buf));
     
          if (error) 
          {
              Serial.println("Could not deserialize the eepromcontent");
              free(buf);
              return;
          }
          WIFI_SSID = String(jsonObj["ssid"]);
          WIFI_PASS = String(jsonObj["password"]);
          initializeWiFiStationControl = true;
          free(buf);
        }
      }
    
      
    }


    bool initializeConfiguration()
    {
        DeserializationError error = deserializeJson(configuration, configurationJSON);
        
        if (error) 
        {
            Serial.println("deserializeJson() failed: ");
            Serial.println(error.f_str());
            return false;
        }
        return true;
    }

    void setup() {


        //initializeTimer(); PWM uses timer1 so we an not use it anymore
        delay(1000);
        Serial.begin(115200);
        Serial.println();

        if(initializeConfiguration() ==true)
        {
          configurationDeserializedValidator = true;
          initializeWiFiAp();
          initializeHttpServer();
          initializeEEPROM();
          retreiveCredentials();
          initializeWebSocketClient();
          //initializeConfiguration();
          initializeGPIOPins();
        }
        else
        {
          Serial.println("Critical system error contact for update");
          configurationDeserializedValidator = false;
          initializeWiFiAp();
          initializeHttpServer();
        }
        
      
        
    }

    void loop() {
      httpServer.handleClient();
      webSocketClient.loop();

      if(maintainWiFiControl == true)
      {
        maintainWiFi();
        maintainWiFiControl = false;
      }
      if(initializeWiFiStationControl == true)
      {
        initializeWiFiStation();
        initializeWiFiStationControl = false;
      }
       if(webSocketClientSendHeartBeatControl == true)
      {
        sendHeartBeat();
        webSocketClientSendHeartBeatControl = false;
      }

      long cT = millis();
      if(cT-lastTimeOnTimeExecuted>5000)
      {
        lastTimeOnTimeExecuted=cT;
        onTime();
      }
    }
