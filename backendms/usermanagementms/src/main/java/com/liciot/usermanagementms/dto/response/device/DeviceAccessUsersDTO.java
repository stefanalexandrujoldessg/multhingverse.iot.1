package com.liciot.usermanagementms.dto.response.device;

import java.util.HashMap;
import java.util.Map;

public class DeviceAccessUsersDTO {
    Map<String, String> accessUsers = new HashMap<>();


    public Map<String, String> getAccessUsers() {
        return accessUsers;
    }

    public void setAccessUsers(Map<String, String> accessUsers) {
        this.accessUsers = accessUsers;
    }

    public DeviceAccessUsersDTO(Map<String, String> accessUsers) {

        this.accessUsers = accessUsers;
    }
    public DeviceAccessUsersDTO(   ) {


    }
    public void  addAccessUser(String userId, String username)
    {
        this.accessUsers.put(userId, username);
    }
}
