package com.messenger.chatty.security.oauth2;

import java.util.Map;

public class GithubResponse implements Oauth2Response{
    private final Map<String, Object> attribute;

    public GithubResponse(Map<String, Object> attribute) {

        this.attribute = attribute;
    }

    @Override
    public String getServiceProvider() {

        return "github";
    }

    @Override
    public String getUniqueUsername() {
        return getServiceProvider() +"_" + attribute.get("id").toString();
    }

    @Override
    public String getName() {

        return attribute.get("login").toString();
    }
    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getProfileImgURL() {
        return attribute.get("avatar_url").toString();
    }
}
