package com.allback.cygiuser.config.oauth.service;

import com.allback.cygiuser.enums.ProviderType;
import com.allback.cygiuser.config.oauth.entity.UserPrincipal;
import com.allback.cygiuser.config.oauth.info.OAuth2UserInfo;
import com.allback.cygiuser.config.oauth.info.OAuth2UserInfoFactory;
import com.allback.cygiuser.entity.Passbook;
import com.allback.cygiuser.entity.Users;
import com.allback.cygiuser.enums.RoleType;
import com.allback.cygiuser.repository.PassbookRepository;
import com.allback.cygiuser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;
  private final PassbookRepository passbookRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User user = super.loadUser(userRequest);

    try {
      return this.process(userRequest, user);
    } catch (AuthenticationException ex) {
      throw ex;
    } catch (Exception ex) {
      log.error("CustomOAuth2UserService loadUser Error: {} ", ex.getMessage());
      throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
    }
  }

  private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
    System.out.println(user.toString());
    ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

    OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
    Users savedUser = userRepository.findUsersByUuid(userInfo.getId());

    if (savedUser != null) {
      updateUser(savedUser, userInfo);
    } else {
      savedUser = createUser(userInfo, providerType);
    }

    return UserPrincipal.create(savedUser, user.getAttributes());
  }

  private Users createUser(OAuth2UserInfo userInfo, ProviderType providerType) {
    Passbook newPassbook = Passbook.builder()
        .cash(5000L)
        .build();

    passbookRepository.save(newPassbook);
    Users user = Users.builder()
        .email(userInfo.getEmail())
        .passbookId(newPassbook)
        .uuid(userInfo.getId())
        .nickname(userInfo.getName())
        .providerType(providerType)
        .profile(userInfo.getImageUrl())
        .role(RoleType.ROLE_USER)
        .build();

    return userRepository.saveAndFlush(user);
  }

  private void updateUser(Users user, OAuth2UserInfo userInfo) {
    if (userInfo.getName() != null && !user.getNickname().equals(userInfo.getName())) {
      user.setNickname(userInfo.getName());
    }
    user.setProfile(user.getProfile());
  }
}
