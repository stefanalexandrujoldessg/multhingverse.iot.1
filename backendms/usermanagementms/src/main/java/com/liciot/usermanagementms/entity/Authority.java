package com.liciot.usermanagementms.entity;


import javax.persistence.Embeddable;

@Embeddable

public class Authority {
    String authority;

    public Authority(String authority) {
        this.authority = authority;
    }

    public Authority() {
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
