package com.allback.cygiuser.config.oauth.info;

import com.allback.cygiuser.enums.ProviderType;
import com.allback.cygiuser.config.oauth.info.impl.KakaoOAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {
  public static OAuth2UserInfo getOAuth2UserInfo(ProviderType providerType, Map<String, Object> attributes) {
    return switch (providerType) {
//      case GOOGLE -> new GoogleOAuth2UserInfo(attributes);
//      case NAVER -> new NaverOAuth2UserInfo(attributes);
      case KAKAO -> new KakaoOAuth2UserInfo(attributes);
      default -> throw new IllegalArgumentException("Invalid Provider Type.");
    };
  }
}
