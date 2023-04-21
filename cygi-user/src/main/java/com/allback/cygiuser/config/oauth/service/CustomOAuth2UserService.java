package com.allback.cygiuser.config.oauth.service;

import com.allback.cygiuser.config.oauth.info.OAuth2UserInfo;
import com.allback.cygiuser.config.oauth.info.OAuth2UserInfoFactory;
import com.allback.cygiuser.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

// https://hyunipad.tistory.com/126
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
//public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {


//    private final UserRepository userRepository;
//    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        System.out.println(userRequest);
        System.out.println("--------------------------------------------- check");
        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest); //Oauth2 정보를 가져온다????
        System.out.println(userRequest);

        return oAuth2User;

//        DB정보 체크

//        아이디 이름 가져오기
//        String registrationId = userRequest.getClientRegistration().getRegistrationId();
//        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
//
////        개발자 설정
//        OAuth2UserInfo userInfo = OAuth2UserInfo.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
//        Users user = saveOrUpdate(userInfo);
//        httpSession.setAttribute("user", new SessionUser(user));

//        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey())), attributes.getAttributes(), attributes.getNameAttributeKey());
    }

//    private Users saveOrUpdate(OAuth2UserInfo userInfo){
//        Users user =  userRepository.findOneByEmail(attributes.getEmail())
//                .map(entity -> entity.update(attributes.getName()))
//                .orElse(attributes.toEntity());
//
//        return userRepository.save(user);
//    }

}
