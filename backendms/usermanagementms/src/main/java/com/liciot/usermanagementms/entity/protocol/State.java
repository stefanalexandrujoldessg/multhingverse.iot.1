package com.liciot.usermanagementms.entity.protocol;

import org.w3c.dom.Attr;

import java.util.List;

public class State {
    List<Attribute> attributes;

    public State(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }
}
