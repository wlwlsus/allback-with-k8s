package com.allback.cygiuser.config.oauth.info;

import com.allback.cygiuser.config.oauth.entity.ProviderType;
import com.allback.cygiuser.entity.Passbook;
import com.allback.cygiuser.entity.Users;
import com.allback.cygiuser.enums.RoleType;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuth2UserInfo {
    private Map<String, Object> attributes; // attributes?
    private String nameAttributeKey; // id
    private String name;
    private ProviderType providerType;

    @Builder
    public OAuth2UserInfo(Map<String, Object> attributes, String nameAttributeKey, String name){
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
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
                .nameAttributeKey(userNameAttributeName) // id
                .attributes(attributes)
                .build();
    }

    public Users toEntity(Passbook passbook){
        return  Users.builder()
                .passbookId(passbook)
                .email(((Map<String, Object>)attributes.get("kakao_account")).get("email").toString())
                .nickname(name)
                .role(RoleType.USER)
                .profile(((Map<String, Object>) attributes.get("properties")).get("profile_image").toString()) // 하나 더 들어가야함
                .uuid(nameAttributeKey)
                .providerType(ProviderType.KAKAO)
                .build();
    }
}
