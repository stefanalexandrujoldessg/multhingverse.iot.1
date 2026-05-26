package com.liciot.humanms.service.user;

import com.liciot.humanms.dto.response.user.UserInitializationDTO;
import com.liciot.humanms.service.ApiConsumer;
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
