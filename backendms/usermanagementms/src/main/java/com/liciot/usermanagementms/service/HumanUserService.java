package com.liciot.usermanagementms.service;

import com.liciot.usermanagementms.dto.InsertHumanUserBody;
import com.liciot.usermanagementms.entity.HumanUser;
import com.liciot.usermanagementms.repository.HumanUserRepository;
import com.liciot.usermanagementms.service.kafka.TopicCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HumanUserService {

    @Autowired
    HumanUserRepository humanUserRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    TopicCreator topicCreator;

@Transactional(isolation = Isolation.SERIALIZABLE)
    public HumanUser insertHumanUser(InsertHumanUserBody insertHumanUserBody)
    {
        HumanUser humanUser = new HumanUser(insertHumanUserBody.getUsername(), passwordEncoder.encode(insertHumanUserBody.getPassword()), insertHumanUserBody.getName());
        List toCreateTopics = new ArrayList();//deviceConfiguration.getState().stream().map((attribute)->{return device.getDeviceId().toString().toLowerCase()+"."+attribute.getId().toLowerCase();}).collect(Collectors.toList());

          this.humanUserRepository.save(humanUser);
          toCreateTopics.add("user.notification."+humanUser.getId().toString() );
        toCreateTopics.add("user.event."+humanUser.getId().toString());
        this.topicCreator.createTopics(toCreateTopics);
        return humanUser;
    }

}
