package com.liciot.humanms.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserAndAuthorityListBody {
  UUID userId;

    List<String> authorityList = new ArrayList<String>();

    public UserAndAuthorityListBody(UUID userId, List<String> authorityList) {
        this.userId = userId;
        this.authorityList = authorityList;
    }

    public UserAndAuthorityListBody(List<String> authorityList) {
        this.authorityList = authorityList;
    }
    public UserAndAuthorityListBody() {
     }
    public List<String> getAuthorityList() {
        return authorityList;
    }

    public void setAuthorityList(List<String> authorityList) {
        this.authorityList = authorityList;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AuthorityListBody{" +
                "authorityList=" + authorityList +
                '}';
    }
}
