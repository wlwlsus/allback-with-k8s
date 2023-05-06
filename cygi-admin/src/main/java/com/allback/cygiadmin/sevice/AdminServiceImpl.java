package com.allback.cygiadmin.sevice;

import com.allback.cygiadmin.dto.response.TokenResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Override
    public TokenResDto getLoginToken(Long userId) {
        return null;
    }
}
