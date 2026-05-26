package com.liciot.usermanagementms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserManagementMs {
    public static void main(String[] args) {
        SpringApplication.run(UserManagementMs.class, args);
        System.out.println(
                System.getProperty("java.class.path"));

    }
}
