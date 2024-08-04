package com.messenger.chatty.security.dto;

import com.messenger.chatty.domain.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Getter
public class CustomUserDetails implements UserDetails, OAuth2User {

    private final Member member;
    private final Set<GrantedAuthority> authorities = new HashSet<>();


    public CustomUserDetails(Member member){
        this.member = member;
        authorities.add(new SimpleGrantedAuthority(member.getRole()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {

        return member.getPassword();
    }

    @Override
    public String getUsername() {

        return member.getUsername();
    }


    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }
    @Override
    public String getName() {
        return member.getName();
    }
}
