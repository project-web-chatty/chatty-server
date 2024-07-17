package com.messenger.chatty.security.oauth2;

import java.util.Map;

public class GoogleResponse implements Oauth2Response{
    private final Map<String, Object> attribute;
    public GoogleResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getServiceProvider() {
        return "google";
    }

    @Override
    public String getUniqueUsername() {
        return getServiceProvider() + "_"+attribute.get("sub").toString();
    }
    @Override
    public String getName() {
        return attribute.get("name").toString();
    }
    @Override
    public String getEmail(){
        return attribute.get("email").toString();
    }

    @Override
    public String getProfileImgURL() {
        return attribute.get("picture").toString();
    }
}
