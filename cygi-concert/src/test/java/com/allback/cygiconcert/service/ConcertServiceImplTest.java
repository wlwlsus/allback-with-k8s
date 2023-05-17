package com.allback.cygiconcert.service;

import com.allback.cygiconcert.client.PaymentServerClient;
import com.allback.cygiconcert.client.UserServerClient;
import com.allback.cygiconcert.dto.request.ConcertReqDto;
import com.allback.cygiconcert.dto.response.ConcertResDto;
import com.allback.cygiconcert.entity.Concert;
import com.allback.cygiconcert.entity.Stage;
import com.allback.cygiconcert.mapper.ConcertMapper;
import com.allback.cygiconcert.repository.ConcertRepository;
import com.allback.cygiconcert.repository.StageRepository;
import com.allback.cygiconcert.util.S3Upload;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@SpringBootTest
class ConcertServiceImplTest {

    private static final Logger logger = Logger.getLogger(ConcertServiceImplTest.class.getName());

    private ConcertService concertService;
    private SeatService seatService;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private StageRepository stageRepository;

    @Autowired
    private ConcertMapper concertMapper;

    @Mock
    private PaymentServerClient paymentServerClient;

//    @Mock
    @Autowired
    private UserServerClient userServerClient;

    @Mock
    private S3Upload s3Upload;

    private Stage stage;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        concertService = spy(new ConcertServiceImpl(concertRepository, stageRepository, concertMapper,
                paymentServerClient, userServerClient, s3Upload));

        seatService = new SeatServiceImpl(concertRepository, stageRepository, paymentServerClient);


        // Stage 객체를 생성하고 DB에 저장
        int price = 10000;
        int col = 10;
        int row = 10;
        String location = "멀티캠퍼스";

        Stage stage = Stage.builder()
                .price(price)
                .row(row)
                .col(col)
                .location(location)
                .build();

        this.stage = stageRepository.save(stage);
        logger.info("stage 저장 완료 : " + stage.getStageId());
    }

    @AfterEach
    public void tearDown() throws Exception {
        // 생성한 seat을 DB에서 삭제
        stageRepository.deleteById(this.stage.getStageId());
    }

    @DisplayName("콘서트 등록 테스트")
    @Test
    public void registerConcert_ValidData_Success() throws Exception {
        // Given
        // Mock 객체 초기화
        MockitoAnnotations.openMocks(this);

        // 테스트할 ConcertReqDto 객체 생성
        ConcertReqDto concertReqDto = ConcertReqDto.builder()
                .userId(1L)
                .stageId(this.stage.getStageId())
                .title("테스트 제목")
                .endDate(LocalDateTime.now())
                .startDate(LocalDateTime.now())
                .content("테스트 내용")
                .build();
        logger.info("stage Id :: " + concertReqDto.getStageId());

        // 이미지 링크에 대한 Mock 응답 설정
        String imgLink = "http://example.com/image.jpg";
        doReturn(imgLink).when(concertService).getImgLink(any(MultipartFile.class));

        logger.info("image link ::: " + concertService.getImgLink(mock(MultipartFile.class)));

        // When
        long concertId = concertService.registerConcert(concertReqDto, mock(MultipartFile.class));

        // Then
        ConcertResDto concertResDto = concertService.getConcert(concertId);
        Assertions.assertThat(concertResDto.getTitle()).isEqualTo("테스트 제목");

        concertService.deleteConcert(concertId);

//        // 주최자 id 확인
//        verify(userServerClient).checkUser(userId);
//
//        // 공연장 id 확인
//        verify(stageRepository).findById(stageId);
//
//        // 이미지 링크 생성 확인
//        verify(concertService).getImgLink(any(MultipartFile.class));
//
//        // 공연 등록 확인
//        verify(concertRepository).save(any(Concert.class));
    }

}
