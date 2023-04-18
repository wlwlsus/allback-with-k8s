package com.allback.cygiuser.entity;


import com.allback.cygiuser.config.oauth.entity.ProviderType;
import com.allback.cygiuser.util.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class Users extends BaseTimeEntity implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id", nullable = false, columnDefinition = "BIGINT UNSIGNED")
  private Long userId;

  @OneToOne(optional = false)
  @JoinColumn(name = "passbook_id", nullable = false)
  private Users passbookId;

  @Column(name = "email", length = 255, nullable = false)
  private String email;

  @Column(name = "nickname", length = 255, nullable = false)
  private String nickname;

  @Column(name = "role", length = 20)
  @ElementCollection(fetch = FetchType.EAGER)
  @Builder.Default
  private List<String> roles = new ArrayList<>();

  @Column(name = "profile")
  private String profile;

  @Column(name = "uuid", length = 30)
  private String uuid;

  @Column(name = "provider", length = 20)
  @Enumerated(EnumType.STRING)
  @NotNull
  private ProviderType providerType;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
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
}
