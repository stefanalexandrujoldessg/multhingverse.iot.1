package com.liciot.usermanagementms.controller;

import com.liciot.usermanagementms.dto.InsertHumanUserBody;
import com.liciot.usermanagementms.entity.HumanUser;
import com.liciot.usermanagementms.entity.User;
import com.liciot.usermanagementms.service.HumanUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crud/humanuser")
@CrossOrigin(origins = "*")
public class CrudHumanUserController {
@Autowired
    HumanUserService humanUserService;

    @PostMapping("/insert")
    public HumanUser insertUser(@RequestBody InsertHumanUserBody insertHumanUserBody)
    {
        System.out.println("[CrudHumanUserController]: "+ insertHumanUserBody);
        HumanUser humanUser =  this.humanUserService.insertHumanUser(insertHumanUserBody);
        System.out.println("[CrudHumanUserController]: "+ humanUser);
        return humanUser;
    }
}
