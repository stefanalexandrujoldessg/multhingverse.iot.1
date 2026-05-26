package com.liciot.usermanagementms.controller;

import com.liciot.usermanagementms.dto.AuthorityListBody;
import com.liciot.usermanagementms.dto.InsertHumanUserBody;
import com.liciot.usermanagementms.dto.UserAndAuthorityListBody;
import com.liciot.usermanagementms.dto.request.UserDeviceConfigurationDTO;
import com.liciot.usermanagementms.dto.response.UserForDeviceDTO;
import com.liciot.usermanagementms.dto.response.user.UserAccessDevicesDTO;
import com.liciot.usermanagementms.dto.response.user.UserAdminDevicesDTO;
import com.liciot.usermanagementms.dto.response.user.UserDevicesConfigurationsDTO;
import com.liciot.usermanagementms.dto.response.user.UserInitializationDTO;
import com.liciot.usermanagementms.entity.HumanUser;
import com.liciot.usermanagementms.entity.UserDeviceConfiguration;
import com.liciot.usermanagementms.repository.SessionRepository;
import com.liciot.usermanagementms.service.HumanUserService;
import com.liciot.usermanagementms.service.UserDeviceConfigurationService;
import com.liciot.usermanagementms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/crud/user")
@CrossOrigin(origins = "*")
public class CrudUserController {
@Autowired
SessionRepository sessionRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserDeviceConfigurationService userDeviceConfigurationService;
    @PostMapping("/addAuthorities")
    public void addAuthoritiesToUser(@RequestBody UserAndAuthorityListBody userAndAuthorityListBody)
    {
        System.out.println("[CrudUserController]: "+ userAndAuthorityListBody);
         this.userService.addAuthoritiesToUser(userAndAuthorityListBody.getUserId(), new AuthorityListBody(userAndAuthorityListBody.getAuthorityList()));
     }
     @GetMapping("/getAll")
    public List<UserForDeviceDTO> getAll()
     {
         return this.userService.findAll();
     }


    @GetMapping("/remTok")
    public void remTok()
    {
          this.sessionRepository.removeExpiredTokens(LocalDateTime.now());
    }



    @GetMapping("/getByToken/force")
    public UserInitializationDTO getUserByToken() {

        this.sessionRepository.removeExpiredTokens(LocalDateTime.now());


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getPrincipal().getClass().cast(authentication.getPrincipal()));

       User userDetails = (User) authentication.getPrincipal();
        UserInitializationDTO userInitializationDTO = this.userService.findByUsername(userDetails.getUsername());
         System.out.println("[!!!]"+userInitializationDTO.getId());//getAdminDevicesIds().stream().map((dev)->{System.out.println(dev); return ;}));
        System.out.println("[!!!]"+userInitializationDTO.getAdminDevicesIds().size());//getAdminDevicesIds().stream().map((dev)->{System.out.println(dev); return ;}));

        for (UUID dev: userInitializationDTO.getAdminDevicesIds() ) {

            System.out.println(dev);
        }
        return userInitializationDTO;

    }
    @GetMapping("/getAdminDevicesByToken")
    public UserAdminDevicesDTO getAdminDevicesByToken() {

        this.sessionRepository.removeExpiredTokens(LocalDateTime.now());


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getPrincipal().getClass().cast(authentication.getPrincipal()));

        User userDetails = (User) authentication.getPrincipal();
        UserAdminDevicesDTO userAdminDevicesDTO = this.userService.getAdminDevicesByUsername(userDetails.getUsername());
       // System.out.println("[!!!]"+userAdminDevicesDTO.getId());//getAdminDevicesIds().stream().map((dev)->{System.out.println(dev); return ;}));
        //System.out.println("[!!!]"+userAdminDevicesDTO.getAdminDevicesIds().size());//getAdminDevicesIds().stream().map((dev)->{System.out.println(dev); return ;}));


        return userAdminDevicesDTO;

    }
    @GetMapping("/getAccessDevicesByToken")
    public UserAccessDevicesDTO getAccessDevicesByToken() {

        this.sessionRepository.removeExpiredTokens(LocalDateTime.now());


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getPrincipal().getClass().cast(authentication.getPrincipal()));

        User userDetails = (User) authentication.getPrincipal();
        UserAccessDevicesDTO userAccessDevicesDTO = this.userService.getAccessDevicesByUsername(userDetails.getUsername());
        // System.out.println("[!!!]"+userAdminDevicesDTO.getId());//getAdminDevicesIds().stream().map((dev)->{System.out.println(dev); return ;}));
        //System.out.println("[!!!]"+userAdminDevicesDTO.getAdminDevicesIds().size());//getAdminDevicesIds().stream().map((dev)->{System.out.println(dev); return ;}));


        return userAccessDevicesDTO;

    }
    @GetMapping("/getDevicesConfigurations/{userId}")
    public UserDevicesConfigurationsDTO getDevicesConfigurations(@PathVariable(name="userId") String userId )

    {
        return this.userDeviceConfigurationService.getByUserId(userId);
    }
    @PostMapping("/postDeviceConfiguration")
    public void postDeviceConfiguration(@RequestBody UserDeviceConfigurationDTO userDeviceConfigurationDTO)

    {
          this.userDeviceConfigurationService.insert(userDeviceConfigurationDTO.getUserId(), userDeviceConfigurationDTO.getDeviceId(), userDeviceConfigurationDTO.getConfigurationJSON());
    }


}
