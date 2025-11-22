# 도메인 정리

## Auth (인증)

### UserAuth
| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | PK |
| name | String | 이름 |
| email | String | 이메일 |
| role | UserAuthRole | 권한 (USER, ADMIN) |
| authProvider | String | OAuth 제공자 |
| authProviderUserId | String | OAuth 제공자 유저 ID (unique) |

**정책**
- 소셜 로그인 시 기본 role은 USER

---

## User (사용자)

### UserProfile
| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | PK |
| name | String | 이름 |
| nickname | String | 닉네임 |
| email | String | 이메일 |
| phoneNumber | String | 전화번호 |
| studentId | String | 학번 (nullable) |
| major | String | 전공 (nullable) |
| gender | Gender | 성별 (nullable) |
| userAuthEntity | UserAuthEntity | FK |

**Gender**: `MALE`, `FEMALE`, `OTHER`, `UNDISCLOSED`

---

## Club (동아리)

### Club
| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | PK |
| name | String | 동아리명 |
| nameEn | String | 동아리명 (영문) |
| description | String | 설명 (nullable) |
| thumbnailImageUrl | String | 썸네일 이미지 (nullable) |
| bannerImageUrl | String | 배너 이미지 (nullable) |
| roomInfo | String | 과방 정보 (nullable) |
| groupChatLink | String | 채팅방 링크 (nullable) |
| groupChatPassword | String | 채팅방 비밀번호 (nullable) |
| dues | Integer | 회비 (nullable) |
| owner | ClubMembershipEntity | FK - 소유자 |
| subscriptionStartDate | LocalDate | 구독 시작일 |
| subscriptionExpireDate | LocalDate | 구독 종료일 |

**정책**
- 동아리 생성 시 구독 기간은 6개월
- 동아리 정보 수정은 owner만 가능

### ClubMembership
| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | PK |
| clubJoinDate | LocalDate | 가입일 |
| clubMemberRole | ClubMemberRole | 역할 |
| userProfile | UserProfileEntity | FK |
| nickname | String | 동아리 내 닉네임 |
| club | ClubEntity | FK |

**ClubMemberRole**: `PRESIDENT`(회장, 1), `VICEPRESIDENT`(부회장, 2), `SECRETARY`(총무, 3), `OFFICER`(임원, 4), `MEMBER`(회원, 5)
- 숫자가 낮을수록 높은 권한

**정책**
- 동아리 생성자는 PRESIDENT 역할로 자동 가입

### ClubApplicationForm
| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | PK |
| club | ClubEntity | FK |
| name | String | 폼 이름 |
| formContent | List\<FormQuestion\> | 질문 목록 (JSON) |
| createdAt | LocalDate | 생성일 |
| applicationCount | Integer | 신청 수 |

**정책**
- 신청서 제출 시 applicationCount 증가

### ClubApplicationSubmission
| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | PK |
| clubApplicationForm | ClubApplicationFormEntity | FK |
| formAnswers | List\<FormAnswer\> | 답변 목록 (JSON) |
| userProfile | UserProfileEntity | FK (unique) |
| applicatedAt | LocalDate | 신청일 |
| applicationStatus | ClubApplicationStatus | 신청 상태 |

**ClubApplicationStatus**: `PENDING`(대기), `APPROVED`(승인), `REJECTED`(거절), `WITHDRAWN`(철회)

**정책**
- 신청 시 기본 상태는 PENDING

---

## Notice (공지사항)

### Notice
| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | PK |
| title | String | 제목 |
| content | String | 내용 (TEXT) |
| isPinned | Boolean | 고정 여부 |
| updatedAt | LocalDate | 수정일 |
| deletedAt | LocalDateTime | 삭제일 (nullable) |
| club | ClubEntity | FK |
| writer | ClubMembershipEntity | FK - 작성자 |

**정책**
- Soft Delete 적용 (deletedAt으로 삭제 여부 판단)
- 조회 시 고정 공지(isPinned=true)가 먼저, 이후 수정일 내림차순
- isPinned 기본값은 false
