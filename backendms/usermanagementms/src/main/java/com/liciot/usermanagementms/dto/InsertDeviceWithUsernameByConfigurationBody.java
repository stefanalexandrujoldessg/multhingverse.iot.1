package com.liciot.usermanagementms.dto;

import java.util.UUID;

public class InsertDeviceWithUsernameByConfigurationBody {

    String configurationJSON;
    String adminUserUsername;// va trevui dupa username este msi usor pt usert cand conf disozitivulawa
   //// String username;
    //String password;

    public InsertDeviceWithUsernameByConfigurationBody(String configurationJSON, String adminUserUsername) {
         this.configurationJSON = configurationJSON;
        this.adminUserUsername = adminUserUsername;
    }

    public InsertDeviceWithUsernameByConfigurationBody() {
    }





    public String getConfigurationJSON() {
        return configurationJSON;
    }

    public void setConfigurationJSON(String configurationJSON) {
        this.configurationJSON = configurationJSON;
    }

    public String getAdminUserUsername() {
        return adminUserUsername;
    }

    public void setAdminUserUsername(String adminUserUsername) {
        this.adminUserUsername = adminUserUsername;
    }
}
