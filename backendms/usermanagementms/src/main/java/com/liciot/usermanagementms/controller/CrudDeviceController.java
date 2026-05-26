package com.liciot.usermanagementms.controller;

import com.liciot.usermanagementms.dto.*;
import com.liciot.usermanagementms.dto.response.DeviceDTO;
import com.liciot.usermanagementms.dto.response.device.DeviceAccessUsersDTO;
import com.liciot.usermanagementms.dto.response.device.DeviceConfigurationDTO;
import com.liciot.usermanagementms.entity.Device;
import com.liciot.usermanagementms.entity.HumanUser;
import com.liciot.usermanagementms.entity.User;
import com.liciot.usermanagementms.service.DeviceService;
import com.liciot.usermanagementms.service.HumanUserService;
import com.liciot.usermanagementms.service.UserService;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/crud/device")
@CrossOrigin(origins = "*")
public class CrudDeviceController {
    @Autowired
    DeviceService deviceService;
    /*
    @PostMapping("/addAuthorities")
    public void addAuthoritiesToUser(@RequestBody UserAndAuthorityListBody userAndAuthorityListBody)
    {
        System.out.println("[CrudUserController]: "+ userAndAuthorityListBody);
        this.userService.addAuthoritiesToUser(userAndAuthorityListBody.getUserId(), new AuthorityListBody(userAndAuthorityListBody.getAuthorityList()));
    }
    @GetMapping("/getAll")
    public List<User> getAll()
    {
        return this.userService.findAll();
    }
    /*


     */
    @PostMapping("/insert")
    public DeviceDTO insert (@RequestBody
                                       InsertDeviceBody insertDeviceBody)
    {
        DeviceDTO device =  this.deviceService.insertDevice(insertDeviceBody);
        if ((device != null)) {
           // device.getAdminUser().setAuthorityList(null);
            //in afara tranzatiei e ok dar hmm nu ar treebui
        }

        return device;

    }


    @PostMapping("/insertByConfiguration")
    public DeviceDTO insert (@RequestBody
                                     InsertDeviceByConfigurationBody insertDeviceByConfigurationBody)
    {
        DeviceDTO device =  this.deviceService.insertDevice(insertDeviceByConfigurationBody);
        if ((device != null)) {
            // device.getAdminUser().setAuthorityList(null);
            //in afara tranzatiei e ok dar hmm nu ar treebui
        }

        return device;

    }




    @PostMapping("/insertWithUsername")
    public DeviceDTO insert (@RequestBody
                                     InsertDeviceWithUsernameBody insertDeviceBody)
    {
        DeviceDTO device =  this.deviceService.insertDevice(insertDeviceBody);
        if ((device != null)) {
            // device.getAdminUser().setAuthorityList(null);
            //in afara tranzatiei e ok dar hmm nu ar treebui
        }

        return device;

    }


    @PostMapping("/insertWithUsernameByConfiguration")
    public DeviceDTO insert (@RequestBody
                                     InsertDeviceWithUsernameByConfigurationBody insertDeviceByConfigurationBody)
    {
        DeviceDTO device =  this.deviceService.insertDevice(insertDeviceByConfigurationBody);
        if ((device != null)) {
            // device.getAdminUser().setAuthorityList(null);
            //in afara tranzatiei e ok dar hmm nu ar treebui
        }

        return device;

    }



    @GetMapping("/getAll")
    public List<DeviceDTO> getAll (
                                      )
    {
      // return this.deviceService.getAll().stream().map((device -> {device.getAdminUser().setAuthorityList(null); return device;})).collect(Collectors.toList());
        return this.deviceService.getAll();

    }
    @GetMapping("/getByUserId")
    public List<Device> getAll (@RequestParam(name = "userId") UUID userId)
    {
        return this.deviceService.findByUserId(userId).stream().map((device -> {device.getAdminUser().setAuthoritySet(null); return device;})).collect(Collectors.toList());


    }
    @PostMapping("/addAccessUsers")
    public void addAccessUsers (@RequestBody AccessUsersForDeviceBody accessUsersForDeviceBody)
    {
 this.deviceService.addAccessUsers(accessUsersForDeviceBody);

    }
    @GetMapping("/addAccessUserByUsername/{deviceId}/{username}")
    public void addAccessUerByUsername (@PathVariable(name = "deviceId") String deviceId , @PathVariable(name = "username")String username)
    {
        this.deviceService.addAccessUserByUsername(deviceId,username);

    }
    @GetMapping("/removeAccessUserByUsername/{deviceId}/{username}")
    public void removeAccessUserByUsername (@PathVariable(name = "deviceId") String deviceId , @PathVariable(name = "username")String username)
    {
        this.deviceService.removeAccessUserByUsername(deviceId,username);

    }
    @PostMapping("/removeAccessUsers")
    public void removeAccessUsers (@RequestBody AccessUsersForDeviceBody accessUsersForDeviceBody)
    {
        this.deviceService.removeAccessUsers(accessUsersForDeviceBody);

    }

    @GetMapping("/getConfigurationById/{deviceId}")
    public DeviceConfigurationDTO getConfigurationById (@PathVariable(name ="deviceId") UUID deviceId  )
    {
        System.out.println(deviceId);
        return this.deviceService.getDeviceConfigurationById(deviceId);

    }
    @GetMapping("/getAccessUsers/{deviceId}")
    public DeviceAccessUsersDTO getDeviceAccessUsers (@PathVariable(name ="deviceId") String deviceId  )
    {
        System.out.println(deviceId);
        return this.deviceService.getAccessUsers(deviceId);

    }
    @GetMapping("/delete/{deviceId}")
    public void delete (@PathVariable(name ="deviceId") String deviceId  )
    {

          this.deviceService.delete(deviceId);

    }
}
