package com.spring.commons.entity.weixin.req;

public class MenuMessage extends BaseMessage {
    private String EventKey;

    public String getEventKey() {
        return EventKey;
    }

    public void setEventKey(String eventKey) {
        EventKey = eventKey;
    }
}
