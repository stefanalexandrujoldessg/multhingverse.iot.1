package com.liciot.humanms.dto;

import java.util.ArrayList;
import java.util.List;

public class AuthorityListBody {
    List<String> authorityList = new ArrayList<String>();

    public AuthorityListBody(List<String> authorityList) {
        this.authorityList = authorityList;
    }
    public AuthorityListBody() {
     }
    public List<String> getAuthorityList() {
        return authorityList;
    }

    public void setAuthorityList(List<String> authorityList) {
        this.authorityList = authorityList;
    }

    @Override
    public String toString() {
        return "AuthorityListBody{" +
                "authorityList=" + authorityList +
                '}';
    }
}
