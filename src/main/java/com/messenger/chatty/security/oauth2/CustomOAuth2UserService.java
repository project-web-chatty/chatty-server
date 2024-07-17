package com.messenger.chatty.security.oauth2;


import com.messenger.chatty.entity.Member;
import com.messenger.chatty.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final SecretId

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 유저는 리다이렉션 URI로 리다이렉트되고. 파라미터로 받은 코드와 토큰을 통해 유저 정보를 갖고 왔을때
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("user : " + oAuth2User);
        System.out.println("attribute : "+ oAuth2User.getAttributes());
        // System.out.println("oAuth2User.getAttributes().get(\"login\") = " + oAuth2User.getAttributes().get("login"));
        // google이거나 github이거나 등등
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

        // detail service 처럼 principal 객체를 반환해주어야 provider가 정상적인 로그인이라고 생각한다
        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getUniqueUsername();
        System.out.println("username = " + username);

        Optional<Member> memberOptional = memberRepository.findByUsername(username);
        Member member;
        if(memberOptional.isEmpty()){
            member = Member.builder()
                    .username(oAuth2Response.getUniqueUsername())
                    .email(oAuth2Response.getEmail())
                    .name(oAuth2Response.getName())
                    .build();
        }
        member = memberOptional.get();
        memberRepository.save(member);
        return new CustomOAuth2User(member);
    }



}
