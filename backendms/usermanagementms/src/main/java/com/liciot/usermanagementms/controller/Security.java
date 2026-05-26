package com.liciot.usermanagementms.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.liciot.usermanagementms.dto.BasicAuthenticationBody;
import com.liciot.usermanagementms.entity.Session;
import com.liciot.usermanagementms.service.security.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.chrono.JapaneseChronology;
import java.util.Objects;
import com.liciot.usermanagementms.service.security.JwtTokenUtil;

@RestController
@RequestMapping("/security")
@CrossOrigin("*")
public class Security {
@Autowired
AuthenticationManager authenticationManager;
@Autowired
JwtTokenUtil jwtTokenUtil;
@Autowired
    SessionService sessionService;
    @GetMapping("/validate")
    public void  validate( ) throws Exception {

        // jwtTokenUtil
    }
    @RequestMapping("/authenticate")
    public String authenticate(@RequestBody BasicAuthenticationBody basicAuthenticationBody) throws Exception {

        System.out.print("[Security] :"+ basicAuthenticationBody.getUsername());
        Authentication authentication = authenticateWithSpringGivenBean(basicAuthenticationBody.getUsername(), basicAuthenticationBody.getPassword());
        UserDetails userDetails = new User(basicAuthenticationBody.getUsername(), "shouldnt care",authentication.getAuthorities());
        Session session = sessionService.createSession(basicAuthenticationBody.getUsername());
        return jwtTokenUtil.generateToken(session.getToken());
         // jwtTokenUtil
    }
    @RequestMapping("/authenticate/getToken")
    public JsonNode authenticateToken(@RequestBody BasicAuthenticationBody basicAuthenticationBody) throws Exception {

        System.out.print("[Security] :"+ basicAuthenticationBody.getUsername());
        Authentication authentication = authenticateWithSpringGivenBean(basicAuthenticationBody.getUsername(), basicAuthenticationBody.getPassword());
        UserDetails userDetails = new User(basicAuthenticationBody.getUsername(), "shouldnt care",authentication.getAuthorities());
        Session session = sessionService.createSession(basicAuthenticationBody.getUsername());
        ObjectMapper objectMapper  = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        response.put("token",jwtTokenUtil.generateToken(session.getToken()) );
        response.put("id",session.getUser().getId().toString() );

        return response;
        // jwtTokenUtil
    }
    @RequestMapping("/logOut")
    public void logOut(@RequestHeader(name = "Authorization") String authorizationHeader) throws Exception {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ") && authorizationHeader.length() > 10) {
            String jwtSessionToken = jwtTokenUtil.getSessionTokenFromToken(authorizationHeader.substring(String.valueOf("Bearer ").length() - 1).trim());
            this.sessionService.removeSessionByByToken(jwtSessionToken);

        }
        else
        {
            throw new Exception("Not good token");
        }
    }
    private Authentication authenticateWithSpringGivenBean(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {//DECI DACA NU PUN authenticatre nu se verifica nimic trece cu succes si se treturneaza tokenul dar atunvci vreau sa stiu ce face methoda asta
           return  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }


}
