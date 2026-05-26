package com.liciot.usermanagementms.entity.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attribute {
    String id;
    String metaType;
    String type;
    ObjectNode value;

    ///String name;
    //String type;

    public Attribute() {
    }

    /***
     * All the values must be represented as string in JSON not the JSON impose this, but my protocol does because no part showul  be aware of anithynk especiqally of the data type sbecause new datqatypes ca  be creates by the users.
     * The only entities that shoul interpret the data types are the endpoints : the device the usr formtend all th virtual deficesa nd so on.
     * so we will not use the capability of json of treprezenting numbers boolean and so on everithing
     * but wait that means we can not use the list of json hmm vor data values i thsink we shoul kep the sjson way because we as the broker we do not care of the value at all
     it is important to keep the json format for data becauseit is simpler


     I do not care ani=ymore what i have said earlier because npw we are doing like this: we actually never care here or in any service (only the virtual devices) what is inside of value
     so we are going to receive on the attribute topic the following
     {msgType:"",
     value: {some json object tha will be sended as it it to the front }
     }
     we will send to the front the fowwoeing {devices: {devId:{stata:{attrId:{value:{}}}}}}

     So we will beUTIFULLY MAP THE VALUE FROM THE json that we receive from topic to a java object and then we set it to other java object that will be assed to the frontedn after iti is transsfomed to json*/


    //String value;
    //List<JsonNode> otherFields;

    public Attribute(String id, String metaType, String type, ObjectNode value) {
        this.id = id;
        this.metaType = metaType;
        this.type = type;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMetaType() {
        return metaType;
    }

    public void setMetaType(String metaType) {
        this.metaType = metaType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ObjectNode getValue() {
        return value;
    }

    public void setValue(ObjectNode value) {
        this.value = value;
    }
}

