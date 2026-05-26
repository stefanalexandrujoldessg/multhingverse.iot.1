package com.liciot.usermanagementms.service;

import com.liciot.usermanagementms.dto.AuthorityListBody;
import com.liciot.usermanagementms.dto.mapper.entitytodto.UserMapper;
import com.liciot.usermanagementms.dto.response.UserForDeviceDTO;
import com.liciot.usermanagementms.dto.response.device.DeviceIdentificationDTO;
import com.liciot.usermanagementms.dto.response.user.UserAccessDevicesDTO;
import com.liciot.usermanagementms.dto.response.user.UserAdminDevicesDTO;
import com.liciot.usermanagementms.dto.response.user.UserInitializationDTO;
import com.liciot.usermanagementms.entity.Authority;
import com.liciot.usermanagementms.entity.Device;
import com.liciot.usermanagementms.entity.User;
import com.liciot.usermanagementms.entity.UserDeviceConfiguration;
import com.liciot.usermanagementms.repository.UserDeviceConfigurationRepository;
import com.liciot.usermanagementms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.beans.Transient;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserDeviceConfigurationRepository userDeviceConfigurationRepository;

    public User findUserByUsername(String username) {
        if (username != null) {
            Optional<User> userOptional = this.userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                return userOptional.get();
            } else {
                //best to throw a resource not found exception and to configure Spring to handle it beautifully
                return null;

            }
        } else {
            //best to throw a resource not found exception and to configure Spring to handle it beautifully
            return null;

        }
    }

    //The persistance context must not be closed in order to LAZY fetch the collection of elements//Transactional
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public User findUserWithAuthoritiesByUsername(String username) {
        if (username != null) {
            Optional<User> userOptional = this.userRepository.findByUsername(username);
            if (userOptional.isPresent()) {

                User user = userOptional.get();
                user.getAuthoritySet();
                System.out.println("Size: " + user.getAuthoritySet().size());
                return user;
            } else {
                //best to throw a resource not found exception and to configure Spring to handle it beautifully
                return null;

            }
        } else {
            //best to throw a resource not found exception and to configure Spring to handle it beautifully
            return null;

        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void addAuthoritiesToUser(UUID userId, AuthorityListBody authorityListBody) {
        User user = userRepository.getById(userId);
        if (user != null) {


            for (String authority : authorityListBody.getAuthorityList()) {
                user.getAuthoritySet().add(new Authority(authority));
            }
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<UserForDeviceDTO> findAll() {
        return this.userRepository.findAll().stream().map((user -> {
            return this.userMapper.formUser(user);
        })).collect(Collectors.toList());

    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UserInitializationDTO findByUsername(String username) {
        Optional<User> userOptional = this.userRepository.findByUsername(username);//.stream().map((user -> {return this.userMapper.formUser(user)    ;})).collect(Collectors.toList());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new UserInitializationDTO(user.getId(), user.getAdminDevices().stream().map((accessDevice) -> {
                return accessDevice.getId();
            }).collect(Collectors.toList()), user.getAccessDevices().stream().map((accessDevice) -> {
                return accessDevice.getId();
            }).collect(Collectors.toList()));
        }
        return null;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UserAdminDevicesDTO getAdminDevicesByUsername(String username) {
        Optional<User> userOptional = this.userRepository.findByUsername(username);//.stream().map((user -> {return this.userMapper.formUser(user)    ;})).collect(Collectors.toList());
        UserAdminDevicesDTO userAdminDevicesDTO = new UserAdminDevicesDTO();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            for(Device device: user.getAdminDevices())
            {
                try{
                    userAdminDevicesDTO.addAdminDevice(device.getId().toString(), device.getConfigurationJSON());
                    Optional<UserDeviceConfiguration> userDeviceConfiguration = this.userDeviceConfigurationRepository.findByUserAndDevice(user, device);
                    if(userDeviceConfiguration.isPresent())
                    {
                        userAdminDevicesDTO.addAdminDevice(device.getId().toString(), device.getConfigurationJSON(), userDeviceConfiguration.get().getConfigurationJSON());

                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

        }
        return userAdminDevicesDTO;
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UserAccessDevicesDTO getAccessDevicesByUsername(String username) {
        Optional<User> userOptional = this.userRepository.findByUsername(username);//.stream().map((user -> {return this.userMapper.formUser(user)    ;})).collect(Collectors.toList());
        UserAccessDevicesDTO userAccessDevicesDTO= new UserAccessDevicesDTO();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            for(Device device: user.getAccessDevices())
            {
                try{
                    userAccessDevicesDTO.addAccessDevice(device.getId().toString(), device.getConfigurationJSON());
                    Optional<UserDeviceConfiguration> userDeviceConfiguration = this.userDeviceConfigurationRepository.findByUserAndDevice(user, device);
                    if(userDeviceConfiguration.isPresent())
                    {
                        userAccessDevicesDTO.addAccessDevice(device.getId().toString(), device.getConfigurationJSON(), userDeviceConfiguration.get().getConfigurationJSON());

                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

        }
        return userAccessDevicesDTO;
    }
}
