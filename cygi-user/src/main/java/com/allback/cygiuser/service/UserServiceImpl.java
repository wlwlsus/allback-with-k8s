package com.allback.cygiuser.service;

import com.allback.cygiuser.config.jwt.JwtTokenProvider;
import com.allback.cygiuser.dto.request.AmountRequest;
import com.allback.cygiuser.dto.request.UserTestReqDto;
import com.allback.cygiuser.dto.response.UserResDto;
import com.allback.cygiuser.entity.Passbook;
import com.allback.cygiuser.entity.Users;
import com.allback.cygiuser.repository.PassbookRepository;
import com.allback.cygiuser.repository.UserRepository;
import com.allback.cygiuser.util.Response;
import com.allback.cygiuser.util.exception.BaseException;
import com.allback.cygiuser.util.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PassbookRepository passbookRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final RedisTemplate<String, String> redisTemplate;

//	private final ReservationRepository reservationRepository;

	@Override
	@Transactional
	public void amount(AmountRequest request) {
		// userId에 해당하는 사용자 조회
		Users user = userRepository.findById(request.getUserId()).orElseThrow(() -> new BaseException(ErrorMessage.NOT_EXIST_USER));

		Passbook passbook = user.getPassbookId();
		// 사용자의 passbook 조회
		Passbook passbookBuilder = Passbook.builder()
				.passbookId(passbook.getPassbookId())
				.cash(passbook.getCash() + request.getAmount())
				.build();

		// 업데이트된 passbook 저장
		passbookRepository.save(passbookBuilder);
	}

	@Override
	public void deductUserCash(long userId, int price) {
		// 1. User 객체 조회
		Optional<Users> optionalUser = userRepository.findById(userId);
		Users user = optionalUser.orElseThrow(() -> new BaseException(ErrorMessage.NOT_EXIST_USER));
		Passbook passbook = user.getPassbookId();
		// 2. 사용자의 계좌에서 금액 차감
		long userCash = passbook.getCash();
		if (userCash < price)
			throw new BaseException(ErrorMessage.NOT_ENOUGH_CASH);

		passbook.setCash(userCash - price);

		// 3. User 객체 저장
		try {
			userRepository.save(user);
		} catch (DataAccessException e) {
			throw new BaseException(ErrorMessage.FAILED_TO_SAVE_USER_INFO);
		}
	}

	@Override
	public Page<UserResDto> getAllUserInfo(int page) {
		System.out.println("회원 전체 목록 반환 service");
		Pageable pageable = PageRequest.of(page, 10);

		Page<Users> original = userRepository.findAll(pageable);

		Page<UserResDto> resPage = original.map(user -> UserResDto.builder()
				.userId(user.getUserId())
				.passbookId(user.getPassbookId())
				.nickname(user.getNickname())
				.email(user.getEmail())
				.provider(user.getProviderType().name())
				.profile(user.getProfile())
				.uuid(user.getUuid())
				.createDate(user.getCreatedDate())
				.modifiedDate(user.getModifiedDate())
				.role(user.getRole())
				.build());

		return resPage;
	}


	@Transactional
	public void updateCash(long userId, long cash) {
		Users user = userRepository.findById(userId)
			.orElseThrow(() -> new BaseException(ErrorMessage.NOT_EXIST_USER));
		Passbook passbook = user.getPassbookId();
		log.info("[updateCash] : 기존 금액, cash : {}", passbook.getCash());
		passbook.setCash(passbook.getCash()+cash);
		log.info("[updateCash] : 바뀐 금액, cash : {}", passbook.getCash());
	}

	@Override
	public int getPoint(long userId) {
		Users user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(ErrorMessage.NOT_EXIST_USER));
		log.info("[getPoint] : 유저 정보 >>> {}", user);
		int point = (int) user.getPassbookId().getCash();
		log.info("[getPoint] : 보유 포인트 >>> {}", point);
		return point;
	}

	public ResponseEntity<?> logout(UserTestReqDto.Logout logout) {
		// 1. Access Token 검증
		if (!jwtTokenProvider.validateToken(logout.getAccessToken())) {
			return Response.badRequest("잘못된 요청입니다.");
		}

		// 2. Access Token 에서 User email 을 가져옵니다.
		Authentication authentication = jwtTokenProvider.getAuthentication(logout.getAccessToken());

		// 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
		if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
			// Refresh Token 삭제
			redisTemplate.delete("RT:" + authentication.getName());
		}

		// 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
		Long expiration = jwtTokenProvider.getExpiration(logout.getAccessToken());
		redisTemplate.opsForValue().set(logout.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

		return Response.ok("로그아웃 되었습니다.");
	}

	@Override
	public Users getUser(long userId) {
		Users user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(ErrorMessage.NOT_EXIST_USER));

		return user;
	}
}