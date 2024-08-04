package com.messenger.chatty.security.oauth2.dto;

public interface Oauth2Response {
    String getServiceProvider();
    String getUniqueUsername();
    String getName();
    String getEmail();
    String getProfileImgURL();

}
