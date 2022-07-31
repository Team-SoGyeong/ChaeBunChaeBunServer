package com.sogyeong.cbcb.defaults.entity.response;

public enum ResultMessage {
    // 0. 로그인
    FAILED_KAKAO("카카오 로그인 실패"),
    SUCCESS_KAKAO("카카오 로그인 성공"),
    NOT_FOUND_LOG("로그인 이력이 없는 사용자 입니다."),
    FOUND_LOG("로그인 이력이 있는 사용자 입니다. "),
    CAN_USE_NIK("사용할 수 없는 닉네임 입니다."),
    CANT_USE_NIK("사용할 수 있는 닉네임 입니다."),
    SUCCESS_LOGOUT("로그아웃 성공"),
    FAILED_LOGOUT("로그아웃 실패"),
    SUCCESS_QUIT("탈퇴 성공"),
    FAILED_QUIT("탈퇴 실패"),

    // 1. 홈

    NO_NEW_LIST("아직 최신 채분이 없어요. 직접 채분을 시작해 신선함을 나눠보세요!"),
    NO_SCRAP_LIST("찜한 채분이 없어요. 참여하고 싶은 채분을 탐색해 찜해보세요!"),
    NO_ENDED_LIST("마감 직전인 채분이 없어요. 아직 많은 채분이 신선함을 유지하고 있어요!"),
    SEARCH_OVER_TWO("두글자 이상 입력 바랍니다."),
    NO_SEARCH_RES("에 대한 검색결과가 없어요! 다시 시도해주세요!"),


    // 2. 게시글
    SUBDIVISION_OK("소분완료 처리되었습니다."),
    SUBDIVISION_ALREADY("이미 소분이 완료된 글입니다."),
    NOT_SUBDIVISION_SELF("자신의 글이 아닌 글을 완료처리 할 수 없습니다."),

    DONATE_OK("기부 처리되었습니다."),
    DONATE_ALREADY("이미 기부가 완료된 글입니다."),
    NOT_DONATE_SELF("자신의 글이 아닌 글을 기부처리 할 수 없습니다."),

    SCRAP_OK("찜하기 성공"),
    SCRAP_FAILED("찜하기 실패"),
    SCRAP_CANCEL_OK("찜 취소 성공"),
    SCRAP_CANCEL_FAILED("찜 취소 실패"),
    UNDEFINE_SCRAP("존재하지 않는 찜내역 입니다. 다시 시도 해주세요"),
    NOT_SCRAP_SELF("자신의 글을 찜할 수 없습니다."),

    NOT_DELETE_OTHERS("타인의 글을 삭제할 수 없습니다. 다시 시도 해주세요"),
    UNDEFINE_COMMENT("존재하지 않는 댓글 입니다. 다시 시도 해주세요"),

    BLIND_OK("더이상 안보이게 하기 성공"),
    BLIND_FAILED("더이상 안보이게 하기 실패"),

    REPORT_OK("신고 하기 성공"),
    REPORT_FAILED("신고 하기 실패"),

    // 3. 마이페이지

    ALREADY_USED_NIK("이미 사용 중인 닉네임 입니다. 다른 닉네임을 입력하세요."),

    // 4. 커뮤니티
    TYPE_ERROR("잘못된 타입명 입력했습니다. post/comm을 입력해주세요."),

    // 5. 공통
    UNDEFINED_USER("존재하지 않는 사용자 입니다. 다시 시도 해주세요"),
    UNDEFINED_ADDRESS("입력된 주소 일련번호는 존재하지 않습니다. 다시 시도 해주세요"),
    UNDEFINED_POST("존재하지 않는 게시글 입니다. 다시 시도 해주세요"),
    UNDEFINED_CATEGORY("입력된 카테고리 정보는 정확하지 않습니다. 다시 시도 해주세요"),
    UNDEFINED_INPUT("입력 정보가 부정확합니다. 다시 시도 부탁드립니다."),
    COMING_SOON("커뮤니티 서비스는 추후에 오픈됩니다."),
    WRITE_OK("작성 성공"),
    WRITE_FAILED("작성 실패"),
    RESULT_OK("출력 성공"),
    RESULT_FAILED("내용 없음"),
    UPDATE_OK("변경/수정 성공"),
    UPDATE_FAILED("변경/수정 실패"),
    DELETE_OK("삭제 성공"),
    DELETE_FAILED("삭제 실패"),
    DEFAULT_MSG("서버 문제 문의 바람");



    private String value;

    ResultMessage(String value) {
            this.value = value;
        }

    public String getKey() {
            return name();
        }
    public String getVal() {
        return this.value;
    }
    public String getEditVal(String e) {
        return  e+this.value;
    }
}
