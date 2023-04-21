package com.allback.cygiuser.config.oauth.info;

import com.allback.cygiuser.entity.Users;
import com.allback.cygiuser.enums.RoleType;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuth2UserInfo {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;

    @Builder
    public OAuth2UserInfo(Map<String, Object> attributes, String nameAttributeKey, String name, String email){
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
    }

    public static OAuth2UserInfo of(String socialName, String userNameAttributeName, Map<String, Object> attributes){
//        kakao
        if("kakao".equals(socialName)){
            return ofKakao("id", attributes);
        }
        return null;
    }

    private static OAuth2UserInfo ofKakao(String userNameAttributeName, Map<String, Object> attributes){
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

        return OAuth2UserInfo.builder()
                .name((String) kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .nameAttributeKey(userNameAttributeName)
                .attributes(attributes)
                .build();
    }

    public Users toEntity(){
        return  Users.builder()
//                .name(name)
                .nickname(name)
                .email(email)
                .role(RoleType.USER)
                .build();
    }
}
