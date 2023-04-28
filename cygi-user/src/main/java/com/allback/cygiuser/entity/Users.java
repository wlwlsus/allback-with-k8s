package com.allback.cygiuser.entity;


import com.allback.cygiuser.config.oauth.entity.ProviderType;
import com.allback.cygiuser.enums.RoleType;
import com.allback.cygiuser.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class Users extends BaseTimeEntity implements OAuth2User, UserDetails, OidcUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id", nullable = false, columnDefinition = "BIGINT UNSIGNED")
  private Long userId;

  @OneToOne(optional = false)
  @JoinColumn(name = "passbook_id", nullable = false)
  private Passbook passbookId;

  @Column(name = "email", length = 255, nullable = false)
  private String email;

  @Column(name = "nickname", length = 255, nullable = false)
  private String nickname;

  @Column(name = "role", length = 20, nullable = false)
  @Enumerated(EnumType.STRING)
  private RoleType role;

  @Column(name = "profile")
  private String profile;

  @Column(name = "uuid", length = 30)
  private String uuid;

  @Column(name = "provider", length = 20, nullable = false)
  @Enumerated(EnumType.STRING)
  private ProviderType providerType;


  @Override
  public Map<String, Object> getClaims() {
    return null;
  }

  @Override
  public OidcUserInfo getUserInfo() {
    return null;
  }

  @Override
  public OidcIdToken getIdToken() {
    return null;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return null;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return null;
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }

  @Override
  public String getName() {
    return null;
  }
}
