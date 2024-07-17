package com.messenger.chatty.security.oauth2;


import com.messenger.chatty.entity.Member;
import com.messenger.chatty.repository.MemberRepository;
import com.messenger.chatty.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.bson.codecs.BsonUndefinedCodec;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 나중에 환경변수로 다루기
    private static final String secretKey = "abcde12345";
    private static final SecureRandom securityRandom = new SecureRandom();

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Oauth2Response oAuth2Response;

        if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else if(registrationId.equals("github")){
            oAuth2Response = new GithubResponse(oAuth2User.getAttributes());
        }
        else {
            return null;
        }

        String username =oAuth2Response.getUniqueUsername();
        Optional<Member> memberOptional = memberRepository.findByUsername(username);
        Member member;
        if(memberOptional.isEmpty()){
            member = Member.builder()
                    .username(username)
                    .password(generateSecretPassword(username))
                    .email(oAuth2Response.getEmail())
                    .name(oAuth2Response.getName())
                    .nickname(oAuth2Response.getName())
                    .profile_img(oAuth2Response.getProfileImgURL())
                    .role("ROLE_USER")
                    .build();
            memberRepository.save(member);
        }
        else {
            member = memberOptional.get();
        }

        return new CustomUserDetails(member);
    }

    private String generateSecretPassword(String userPk){
        return bCryptPasswordEncoder.encode(userPk+secretKey+securityRandom.nextInt());
    }



}
