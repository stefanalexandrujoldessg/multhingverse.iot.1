package com.liciot.usermanagementms.controller;

 
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class TestController {
    @RequestMapping("/ping")
    @PreAuthorize("@mySecurityService.hasPingPermission(authentication,#id)")
    public ResponseEntity<String> ping(@RequestParam(name = "id") String id)
    {
        return new ResponseEntity<String>("pong", HttpStatus.OK);
    }

}