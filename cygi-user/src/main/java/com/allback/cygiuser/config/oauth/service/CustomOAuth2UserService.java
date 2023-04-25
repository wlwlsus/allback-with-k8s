package com.allback.cygiuser.config.oauth.service;

import com.allback.cygiuser.config.oauth.info.OAuth2UserInfo;
import com.allback.cygiuser.entity.Passbook;
import com.allback.cygiuser.entity.Users;
import com.allback.cygiuser.repository.PassbookRepository;
import com.allback.cygiuser.repository.UserRepository;
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
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
//public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {


    private final UserRepository<Users> userRepository;
    private final PassbookRepository<Passbook> passbookRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        System.out.println("Access Token : " + userRequest.getAccessToken()); // org.springframework.security.oauth2.core.OAuth2AccessToken@6ac383dd
        System.out.println("client id : " + userRequest.getClientRegistration().getClientId()); // 6a1d3bc13059fff7378e513ede3a36a2
        System.out.println("client name : " + userRequest.getClientRegistration().getClientName()); // Kakao
        System.out.println("client secret : " + userRequest.getClientRegistration().getClientSecret()); // yCeJ01jCXTKvKWDITAM7KsV44YLFr5Ky
        System.out.println("client authentication method : " + userRequest.getClientRegistration().getClientAuthenticationMethod()); // org.springframework.security.oauth2.core.ClientAuthenticationMethod@2590a0
        System.out.println("registration id : " + userRequest.getClientRegistration().getRegistrationId()); // kakao
        System.out.println("authorization grant type : " + userRequest.getClientRegistration().getAuthorizationGrantType()); // org.springframework.security.oauth2.core.AuthorizationGrantType@5da5e9f3
        System.out.println("provider details : " + userRequest.getClientRegistration().getProviderDetails()); // org.springframework.security.oauth2.client.registration.ClientRegistration$ProviderDetails@9b28173
        System.out.println("scopes : " + userRequest.getClientRegistration().getScopes()); // [profile_nickname, account_email, profile_image]
        System.out.println("redirect uri : " + userRequest.getClientRegistration().getRedirectUri()); // http://localhost:8080/login/oauth2/code/kakao


        System.out.println("--------------------------------------------- 해당 정보를 client로 보내야 할것으로 보입니다");
        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest); //Oauth2 정보를 가져온다????

        System.out.println("Name : " + oAuth2User.getName());
        System.out.println("Attributes : " + oAuth2User.getAttributes());
        System.out.println("email : " + ((Map<String, Object>) oAuth2User.getAttributes().get("kakao_account")).get("email"));
        System.out.println("Authorities : " + oAuth2User.getAuthorities());


//        return oAuth2User;

//        DB정보 체크

//       userRequest로부터 아이디 이름 가져오기
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // kakao
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        System.out.println("registrationId : " + registrationId);
        System.out.println("userNameAttributeName : " + userNameAttributeName);

//        return null;

//        개발자 설정
        OAuth2UserInfo userInfo = OAuth2UserInfo.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        Users user = saveOrUpdate(userInfo);

        System.out.println(" >>>>>>>>>>>> " + userInfo.getAttributes());

//        세션은 사용하지 않는다
//        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString())), userInfo.getAttributes(), userInfo.getNameAttributeKey());
    }

    private Users saveOrUpdate(OAuth2UserInfo userInfo){
        System.out.println("-------------------- save or update");
//        System.out.println(userInfo.getAttributes().get("kakao_account"));

        Users user =  userRepository.findOneByuuid(userInfo.getAttributes().get("id").toString());
//        System.out.println("--------------------" + user.toString());

        if(user == null) {
            Passbook passbook = Passbook.builder()
                    .cash(500000L)
//                    .accountNumber(userInfo.getAttributes().get("id").toString() + userInfo.getAttributes().get("connected_at"))
                    .build();
            passbookRepository.save(passbook);

            user = userInfo.toEntity(passbook);
        }
        else user.update(userInfo.getAttributes().get("nickname").toString());

        System.out.println(user);

        return userRepository.save(user);
    }

}
