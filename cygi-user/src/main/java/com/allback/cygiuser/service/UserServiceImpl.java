package com.allback.cygiuser.service;

import com.allback.cygiuser.dto.request.AmountRequest;
import com.allback.cygiuser.dto.response.UserResDto;
import com.allback.cygiuser.entity.Passbook;
import com.allback.cygiuser.entity.Users;
import com.allback.cygiuser.repository.PassbookRepository;
import com.allback.cygiuser.repository.UserRepository;
import com.allback.cygiuser.util.exception.BaseException;
import com.allback.cygiuser.util.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PassbookRepository passbookRepository;
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

		List<Users> list = userRepository.findAll();

//		System.out.println(list);

		List<UserResDto> resList = list.stream().map(e -> UserResDto.builder()
						.userId(e.getUserId())
						.passbokId(e.getPassbookId())
						.nickname(e.getNickname())
						.email(e.getEmail())
						.provider(e.getProviderType().name())
						.profile(e.getProfile())
						.uuid(e.getUuid())
						.createDate(e.getCreatedDate())
						.modifiedDate(e.getModifiedDate())
						.role(e.getRole())
						.build())
				.collect(Collectors.toList());

		//		list to page
		PageRequest pageRequestForList = PageRequest.of(page, 10);
		int start = (int) pageRequestForList.getOffset();
		int end = Math.min((start + pageRequestForList.getPageSize()), resList.size());
		Page<UserResDto> resPage = new PageImpl<>(resList.subList(start, end),
				pageRequestForList, resList.size());

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
}