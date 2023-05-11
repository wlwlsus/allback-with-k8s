package com.allback.cygiadmin.dto.response;

import com.allback.cygiadmin.util.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserResDto {

    private Long userId;
    private Map<String, Object> passbookId;
    private String nickname;
    private String email;
    private RoleType role;
    private String profile;
    private String uuid;
    private String provider;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;

}
