package com.liciot.devicems.service.user;

import com.liciot.devicems.dto.response.user.UserInitializationDTO;
import com.liciot.devicems.service.ApiConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    ApiConsumer apiConsumer;

    public UserInitializationDTO authorizeAndGetUserByToken(String token)
    {
        return  null;
    }
}
