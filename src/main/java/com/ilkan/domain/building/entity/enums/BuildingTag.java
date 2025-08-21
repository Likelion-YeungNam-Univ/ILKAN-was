package com.ilkan.domain.building.entity.enums;

public enum BuildingTag {

    OFFICE_SPACE("공유 오피스"),       // 1,2,3 묶음 (오피스/회의실/스터디룸)
    PHOTO_STUDIO("촬영 스튜디오"),       // 4 촬영 스튜디오
    POPUP_STORE("팝업 스토어"),        // 5 팝업스토어
    PARTY_ROOM("파티룸"),         // 7 파티룸
    RECORDING_STUDIO("녹음실/음악작업실"),    // 12 녹음실/음악작업실
    ETC("기타");

    private final String kor;

    BuildingTag(String kor) { this.kor = kor; }

    public String getKor() { return kor; }

    public static String toLabel(BuildingTag tag) {
        return tag == null ? ETC.kor : tag.getKor();
    }
}
