package com.woohakdong.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.woohakdong.api.dto.request.ClubItemRegisterRequest;
import com.woohakdong.domain.club.infrastructure.storage.ClubRepository;
import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.club.model.ClubRegisterCommand;
import com.woohakdong.domain.clubitem.infrastructure.storage.repository.ClubItemRepository;
import com.woohakdong.domain.clubitem.model.ClubItemCategory;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ClubItemIntegrationTest {

    @MockitoBean
    private FirebaseAuth firebaseAuth;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ClubItemRepository clubItemRepository;

    private Long clubId;

    @BeforeEach
    void setUp() {
        clubItemRepository.deleteAll();
        clubRepository.deleteAll();

        ClubRegisterCommand command = new ClubRegisterCommand(
                "테스트동아리",
                "testclub",
                "테스트 동아리입니다",
                null,
                null,
                "팔달관 101호",
                null,
                null,
                10000
        );
        ClubEntity club = ClubEntity.create(command, LocalDate.now());
        ClubEntity savedClub = clubRepository.save(club);
        clubId = savedClub.getId();
    }

    @Test
    @DisplayName("물품 등록 후 조회 - 전체 플로우 테스트")
    @WithMockUser
    void registerAndGetClubItems() throws Exception {
        // given: 물품 등록 요청 데이터
        ClubItemRegisterRequest request = new ClubItemRegisterRequest(
                "캠핑 텐트",
                "https://example.com/tent.jpg",
                "4인용 캠핑 텐트입니다.",
                "동아리방 캐비닛 3번",
                ClubItemCategory.SPORT,
                7
        );

        // when & then: 물품 등록
        mockMvc.perform(post("/api/clubs/{clubId}/items", clubId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clubItemId").isNumber());

        // when & then: 물품 목록 조회
        mockMvc.perform(get("/api/clubs/{clubId}/items", clubId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].name").value("캠핑 텐트"))
                .andExpect(jsonPath("$.data[0].category").value("SPORT"))
                .andExpect(jsonPath("$.data[0].rentalMaxDay").value(7))
                .andExpect(jsonPath("$.data[0].available").value(true))
                .andExpect(jsonPath("$.data[0].using").value(false));
    }

    @Test
    @DisplayName("여러 물품 등록 후 카테고리로 필터링 조회")
    @WithMockUser
    void registerMultipleItemsAndFilterByCategory() throws Exception {
        // given: 여러 물품 등록
        ClubItemRegisterRequest sportItem = new ClubItemRegisterRequest(
                "축구공", null, "축구공입니다", "창고", ClubItemCategory.SPORT, 3
        );
        ClubItemRegisterRequest digitalItem = new ClubItemRegisterRequest(
                "노트북", null, "맥북 프로", "사무실", ClubItemCategory.DIGITAL, 7
        );
        ClubItemRegisterRequest bookItem = new ClubItemRegisterRequest(
                "자바의 정석", null, "자바 교재", "책장", ClubItemCategory.BOOK, 14
        );

        // 물품들 등록
        mockMvc.perform(post("/api/clubs/{clubId}/items", clubId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sportItem)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/clubs/{clubId}/items", clubId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(digitalItem)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/clubs/{clubId}/items", clubId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookItem)))
                .andExpect(status().isOk());

        // when & then: 전체 조회 - 3개
        mockMvc.perform(get("/api/clubs/{clubId}/items", clubId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(3));

        // when & then: SPORT 카테고리로 필터링 - 1개
        mockMvc.perform(get("/api/clubs/{clubId}/items", clubId)
                        .param("category", "SPORT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].name").value("축구공"));

        // when & then: DIGITAL 카테고리로 필터링 - 1개
        mockMvc.perform(get("/api/clubs/{clubId}/items", clubId)
                        .param("category", "DIGITAL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].name").value("노트북"));
    }

    @Test
    @DisplayName("키워드로 물품 검색")
    @WithMockUser
    void searchItemsByKeyword() throws Exception {
        // given: 물품 등록
        ClubItemRegisterRequest item1 = new ClubItemRegisterRequest(
                "캠핑 텐트", null, null, null, ClubItemCategory.SPORT, 7
        );
        ClubItemRegisterRequest item2 = new ClubItemRegisterRequest(
                "캠핑 의자", null, null, null, ClubItemCategory.SPORT, 7
        );
        ClubItemRegisterRequest item3 = new ClubItemRegisterRequest(
                "노트북", null, null, null, ClubItemCategory.DIGITAL, 7
        );

        mockMvc.perform(post("/api/clubs/{clubId}/items", clubId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item1)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/clubs/{clubId}/items", clubId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item2)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/clubs/{clubId}/items", clubId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item3)))
                .andExpect(status().isOk());

        // when & then: "캠핑" 키워드로 검색 - 2개
        mockMvc.perform(get("/api/clubs/{clubId}/items", clubId)
                        .param("keyword", "캠핑"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2));

        // when & then: "노트북" 키워드로 검색 - 1개
        mockMvc.perform(get("/api/clubs/{clubId}/items", clubId)
                        .param("keyword", "노트북"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].name").value("노트북"));
    }

    @Test
    @DisplayName("필수 필드 누락 시 등록 실패")
    @WithMockUser
    void registerItemWithMissingRequiredFields() throws Exception {
        // given: 필수 필드 누락된 요청
        String invalidRequest = """
                {
                    "name": "",
                    "category": null,
                    "rentalMaxDay": null
                }
                """;

        // when & then: 유효성 검증 실패
        mockMvc.perform(post("/api/clubs/{clubId}/items", clubId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }
}
