package com.liciot.usermanagementms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.liciot.usermanagementms.dto.request.UserDeviceConfigurationDTO;
import com.liciot.usermanagementms.dto.response.user.UserDevicesConfigurationsDTO;
import com.liciot.usermanagementms.entity.Device;
import com.liciot.usermanagementms.entity.User;
import com.liciot.usermanagementms.entity.UserDeviceConfiguration;
import com.liciot.usermanagementms.repository.DeviceRepository;
import com.liciot.usermanagementms.repository.UserDeviceConfigurationRepository;
import com.liciot.usermanagementms.repository.UserRepository;
import com.liciot.usermanagementms.service.kafka.TopicCreator;
import com.liciot.usermanagementms.service.kafka.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserDeviceConfigurationService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    UserDeviceConfigurationRepository userDeviceConfigurationRepository;
    @Autowired
    KafkaProducer kafkaProducer;

    @Transactional
    public UserDeviceConfiguration insert(String userId, String deviceId, String configurationJSON)
    {
        if(userId!= null && deviceId != null & configurationJSON!= null)
        {
            UUID uId = UUID.fromString(userId);
            UUID dId = UUID.fromString(deviceId);

            Optional<User> userOptional = userRepository.findById(uId);
            Optional<Device> deviceOptional = deviceRepository.findById(dId);
            if (userOptional.isPresent() && deviceOptional.isPresent()) {
                User user = userOptional.get();
                Device device = deviceOptional.get();

                UserDeviceConfiguration userDeviceConfiguration ;

                Optional<UserDeviceConfiguration> userDeviceConfigurationOptional = this.userDeviceConfigurationRepository.findByUserAndDevice(user,device);
                if(userDeviceConfigurationOptional.isPresent())
                {
                    userDeviceConfiguration = userDeviceConfigurationOptional.get();
                }
                else {
                    userDeviceConfiguration = new UserDeviceConfiguration();
                }
                userDeviceConfiguration.setUser(user);
                userDeviceConfiguration.setDevice(device);
                userDeviceConfiguration.setConfigurationJSON(configurationJSON);
                this.userDeviceConfigurationRepository.save(userDeviceConfiguration);

                try {
                    ObjectMapper objectMapper = new ObjectMapper();

                    ObjectNode recordo = objectMapper.createObjectNode();
                    ObjectNode userDevice = objectMapper.createObjectNode();
                    userDevice.put(device.getId().toString(),configurationJSON);
                    recordo.put("type", "userDeviceConfigurationUpdated");
                    recordo.set("userDeviceConfigurationUpdated", userDevice);
                    String record = objectMapper.writer().writeValueAsString(recordo);
                    this.kafkaProducer.produceRecord("user.event." + user.getId(), record);
                }catch(Exception e){
                    e.printStackTrace();
                }
                return  null;
            }
        }
        return null;
    }
    @Transactional

    public UserDevicesConfigurationsDTO getByUserId(String userId)
    {
        UserDevicesConfigurationsDTO userDeviceConfigurationDTO = new UserDevicesConfigurationsDTO();
        if(userId!= null )
        {
            UUID uId = UUID.fromString(userId);


            Optional<User> userOptional = userRepository.findById(uId);

            if (userOptional.isPresent() ) {
                User user = userOptional.get();

                List<UserDeviceConfiguration> userDeviceConfiguration = this.userDeviceConfigurationRepository.findByUser(user);

                 for(UserDeviceConfiguration iterator: userDeviceConfiguration)
                 {

                     userDeviceConfigurationDTO.addDeviceWithConfiguration(iterator.getDevice().getId().toString(), iterator.getConfigurationJSON());

                 }

            }
        }
        return userDeviceConfigurationDTO;
    }

}
