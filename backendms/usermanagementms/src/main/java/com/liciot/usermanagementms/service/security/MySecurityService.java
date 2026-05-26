package com.liciot.usermanagementms.service.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("mySecurityService")
public class MySecurityService {
    public boolean hasPingPermission(Authentication authentication, String id ) {
        System.out.println("[MySecurityService.hasPingPermission]: "+ authentication.getAuthorities());
        System.out.println("[MySecurityService.hasPingPermission]: "+ id);

        return true;
    }
}