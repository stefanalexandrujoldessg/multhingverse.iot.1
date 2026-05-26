package com.liciot.usermanagementms.dto;

import java.util.UUID;

public class InsertDeviceByConfigurationBody {

    String configurationJSON;
    UUID adminUserId;// va trevui dupa username este msi usor pt usert cand conf disozitivulawa
   //// String username;
    //String password;

    public InsertDeviceByConfigurationBody(  String configurationJSON, UUID adminUserId) {
         this.configurationJSON = configurationJSON;
        this.adminUserId = adminUserId;
    }

    public InsertDeviceByConfigurationBody() {
    }





    public String getConfigurationJSON() {
        return configurationJSON;
    }

    public void setConfigurationJSON(String configurationJSON) {
        this.configurationJSON = configurationJSON;
    }

    public UUID getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(UUID adminUserId) {
        this.adminUserId = adminUserId;
    }
}
