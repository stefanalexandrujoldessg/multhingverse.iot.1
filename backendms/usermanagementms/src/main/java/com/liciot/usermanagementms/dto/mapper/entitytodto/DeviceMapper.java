package com.liciot.usermanagementms.dto.mapper.entitytodto;

import com.liciot.usermanagementms.dto.response.DeviceDTO;
import com.liciot.usermanagementms.dto.response.UserForDeviceDTO;
import com.liciot.usermanagementms.entity.Device;
import com.liciot.usermanagementms.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import java.util.stream.Collectors;
@Component
public class DeviceMapper {
@Autowired
UserMapper userMapper;
   public DeviceDTO fromDevice(Device device)
    {
        return new DeviceDTO(device.getId(),device.getConfigurationJSON(),this.userMapper.formUser(device.getAdminUser()), (device.getAccessUsers()==null)?null:device.getAccessUsers().stream().map((user -> {return this.userMapper.formUser(user);})).collect(Collectors.toList())) ;
    }
}
