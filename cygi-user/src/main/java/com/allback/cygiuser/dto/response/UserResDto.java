package com.allback.cygiuser.dto.response;

import com.allback.cygiuser.entity.Passbook;
import com.allback.cygiuser.enums.RoleType;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserResDto {
    private long userId;
    private Passbook passbookId;
    private String nickname;
    private String email;
    private RoleType role;
    private String profile;
    private String uuid;
    private String provider;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;
}
