package com.liciot.devicems.controller;

import org.apache.coyote.Request;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@CrossOrigin("*")

public class ServicePingController {

    @RequestMapping("/ping")
    public ResponseEntity<String> ping(Request request, Response response)
    {
        return new ResponseEntity<String>("alive", HttpStatus.OK);
    }

}
