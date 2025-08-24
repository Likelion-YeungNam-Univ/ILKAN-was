    package com.ilkan.domain.building.dto;

    import com.ilkan.domain.building.entity.Building;
    import io.swagger.v3.oas.annotations.media.Schema;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Getter;

    import java.time.format.DateTimeFormatter;
    import java.util.ArrayList;
    import java.util.List;

    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "공간 상세 조회 응답 DTO")
    public class BuildingDetailResDto {

        @Schema(description = "건물 ID", example = "1")
        private final Long id;

        @Schema(description = "건물주 이름", example = "이나현")
        private final String owner;

        @Schema(description = "건물주 프로필 사진", example = "https://cdn.example.com/u/3/profile.jpg")
        private final String  profileImage;

        @Schema(description = "빌딩 제목", example = "경산시 공유 오피스 회의실")
        private final String building_name;

        @Schema(description = "UI 표기용 유형 라벨", example = "공유 오피스")
        private final String typeLabel;

        @Schema(description = "건물 태그", example = "OFFICE_SPACE")
        private final String tag;

        @Schema(description = "건물 주소", example = "경북 경산시 조영동 348-19")
        private final String address;

        @Schema(description = "가격 정보")
        private final Price price;

        @Schema(description = "이미지 정보")
        private final Images images;

        @Schema(description = "연락처")
        private final Contact contact;

        @Schema(description = "체크인 시간(HH:mm)", example = "15:00")
        private final String checkIn;

        @Schema(description = "체크아웃 시간(HH:mm)", example = "11:00")
        private final String checkOut;

        @Schema(description = "상세 설명", example = "무료 와이파이, 화이트보드 제공...")
        private final String description;


        @Getter
        @Builder
        @AllArgsConstructor
        @Schema(description = "가격")
        public static class Price {
            @Schema(description = "금액", example = "50000")
            private final Long amount;
            @Schema(description = "통화", example = "KRW")
            private final String currency;
            @Schema(description = "단위", example = "DAY")
            private final String unit;
        }

        @Getter
        @Builder
        @AllArgsConstructor
        @Schema(description = "이미지")
        public static class Images {
            @Schema(description = "대표 이미지", example = "https://.../cover.jpg")
            private final String cover;
            @Schema(description = "갤러리(0~2장)", example = "[\"https://.../1.jpg\",\"https://.../2.jpg\"]")
            private final List<String> gallery;
        }

        @Getter
        @Builder
        @AllArgsConstructor
        @Schema(description = "연락처")
        public static class Contact {
            @Schema(description = "이메일", example = "highfive@naver.com")
            private final String email;
            @Schema(description = "전화번호", example = "010-0000-5555")
            private final String phone;
        }

        public static BuildingDetailResDto fromEntity(
                Building b,
                String typeLabelKor,
                String coverUrl,
                String sub1Url,
                String sub2Url
        ) {
            DateTimeFormatter HHmm = DateTimeFormatter.ofPattern("HH:mm");

            String in  = b.getCheckInTime()  != null ? b.getCheckInTime().format(HHmm)  : null;
            String out = b.getCheckOutTime() != null ? b.getCheckOutTime().format(HHmm) : null;

            List<String> gallery = new ArrayList<>(2);
            if (sub1Url != null && !sub1Url.isBlank()) gallery.add(sub1Url);
            if (sub2Url != null && !sub2Url.isBlank()) gallery.add(sub2Url);

            return BuildingDetailResDto.builder()
                    .id(b.getId())
                    .owner(b.getOwner().getName())
                    .profileImage(b.getOwner().getProfileImage())
                    .building_name(b.getBuildingName())
                    .typeLabel(typeLabelKor)                    // enum.getKor() 결과
                    .tag(b.getBuildingTag() != null ? b.getBuildingTag().name() : null)
                    .address(b.getBuildingAddress())
                    .price(Price.builder().amount(b.getBuildingPrice()).currency("KRW").unit("DAY").build())
                    .images(Images.builder().cover(coverUrl).gallery(gallery).build())
                    .contact(Contact.builder().email(b.getEmail()).phone(b.getPhoneNumber()).build())
                    .checkIn(in)
                    .checkOut(out)
                    .description(b.getBuildingDescription())
                    .build();
        }
    }
