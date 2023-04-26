package com.allback.cygiadmin.sevice;

import com.allback.cygiadmin.dto.response.TokenResDto;

public interface AdminService {

    TokenResDto getLoginToken(Long userId);
}
