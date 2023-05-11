package com.allback.cygiadmin.service;

import com.allback.cygiadmin.dto.response.TokenResDto;

public interface AdminService {

    TokenResDto getLoginToken(Long userId);
}
