package com.liciot.usermanagementms.dto.mapper.entitytodto;

import com.liciot.usermanagementms.dto.response.UserForDeviceDTO;
import com.liciot.usermanagementms.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public  UserForDeviceDTO formUser(User user)
    {
        return new UserForDeviceDTO(user.getId(), user.getUsername());
    }
}
