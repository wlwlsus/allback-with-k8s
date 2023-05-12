package com.allback.cygiconcert.service;

import com.allback.cygiconcert.client.PaymentServerClient;
import com.allback.cygiconcert.dto.request.ConcertReqDto;
import com.allback.cygiconcert.dto.response.ConcertPageResDto;
import com.allback.cygiconcert.dto.response.ConcertResDto;
import com.allback.cygiconcert.entity.Concert;
import com.allback.cygiconcert.entity.Stage;
import com.allback.cygiconcert.mapper.ConcertMapper;
import com.allback.cygiconcert.repository.ConcertRepository;
import com.allback.cygiconcert.repository.StageRepository;
import com.allback.cygiconcert.util.S3Upload;
import com.allback.cygiconcert.util.exception.BaseException;
import com.allback.cygiconcert.util.exception.ErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConcertServiceImplTest {

    private ConcertService concertService;

    @Mock
    private ConcertRepository concertRepository;

    @Mock
    private StageRepository stageRepository;

    @Mock
    private ConcertMapper concertMapper;

    @Mock
    private PaymentServerClient paymentServerClient;

    @Mock
    private S3Upload s3Upload;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        concertService = new ConcertServiceImpl(concertRepository, stageRepository, concertMapper,
                paymentServerClient, s3Upload);

        // Stage 객체를 생성하고 DB에 저장하는 로직 추가
        Stage stage = Stage.builder()
                .stageId(1L)
                .build();

        when(stageRepository.save(any(Stage.class))).thenReturn(stage);
    }

    @DisplayName("콘서트 등록 테스트")
    @Test
    public void registConcert_ValidData_Success() throws Exception {
        // Given
        ConcertReqDto concertReqDto = new ConcertReqDto();
        concertReqDto.setStageId(1L);

        MultipartFile image = mock(MultipartFile.class);
        when(image.getOriginalFilename()).thenReturn("test.jpg");

        Stage stage = new Stage();
        stage.setStageId(1L);
        when(stageRepository.findById(1L)).thenReturn(Optional.of(stage));

        String imgLink = "https://example.com/test.jpg";
        when(s3Upload.uploadFileV1(anyString(), any())).thenReturn(imgLink);

        Concert concert = new Concert();
        when(concertMapper.toEntity(concertReqDto)).thenReturn(concert);

        // When
        concertService.registConcert(concertReqDto, image);

        // Then
        verify(stageRepository).findById(1L);
        verify(s3Upload).uploadFileV1(anyString(), any());
        verify(concertRepository).save(concert);
    }

    @Test
    void getConcertPage() {
    }

    @Test
    void getConcert() {
    }

    @Test
    void getEndedConcert() {
    }

    @Test
    void getUserId() {
    }

    @Test
    void getConcertTitle() {
    }
}