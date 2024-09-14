package io.github.aikusoni.ats.spring.mvcstandard.model.view;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.aikusoni.ats.spring.core.common.BaseErrorCode;
import io.github.aikusoni.ats.spring.core.constants.ErrorCode;
import io.github.aikusoni.ats.spring.core.constants.NoticeLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class ATSResponseBody<T> {
    // 에러코드
    private BaseErrorCode errorCode;
    // 에러별명 (에러 위치 추적에 사용)
    private String errorAlias;
    // 결과 혹은 에러 메시지를 담는다. (예: "사용자 등록 처리가 완료 됐습니다.", "파일 처리 중 문제가 발생했습니다.")
    private String message;
    // 에러 발생시 에러 상세 정보를 담는다
    private Map<String, Object> errorDetail;
    // 실제 데이터를 담는다.
    private T data;
    // notice의 레벨
    @Builder.Default
    private String noticeLevel = NoticeLevel.INFO;
    // API를 호출하는 시스템에 알려줄 부가 정보를 넣는다. (예: API 지원이 2099.12.31에 끝남을 안내)
    private String notice;

    // 응답 헤더를 넣는다.
    @JsonIgnore
    private Map<String, String> headers;

    public ResponseEntity<ATSResponseBody<T>> toResponseEntity() {
        return ResponseEntity.status(errorCode.getStatusCode())
                .headers(getHttpHeaders())
                .body(this);
    }

    public ResponseEntity<ATSResponseBody<T>> toResponseEntity(int httpStatusCode) {
        return ResponseEntity.status(httpStatusCode)
                .headers(getHttpHeaders())
                .body(this);
    }

    public ATSResponseBody<T> headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public ATSResponseBody<T> addHeader(String key, String value) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        this.headers.put(key, value);
        return this;
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        if (this.headers != null) {
            headers.setAll(this.headers);
        }
        return headers;
    }

    public static <T> ATSResponseBody<T> ok(T data, String message) {
        return of(ErrorCode.NO_ERROR, data, message);
    }

    public static <T> ATSResponseBody<T> ok(T data) {
        return of(ErrorCode.NO_ERROR, data, null);
    }

    public static <T> ATSResponseBody<T> of(BaseErrorCode errorCode, T data, String message) {
        return ATSResponseBody.<T>builder()
                .errorCode(errorCode)
                .data(data)
                .message(message)
                .build();
    }

    public static <T> ATSResponseBody<T> error(BaseErrorCode errorCode, String errorAlias, String message) {
        return error(errorCode, errorAlias, message, null);
    }

    public static <T> ATSResponseBody<T> error(BaseErrorCode errorCode, String errorAlias, String message, Map<String, Object> errorDetail) {
        return ATSResponseBody.<T>builder()
                .errorCode(errorCode)
                .errorAlias(errorAlias)
                .message(message)
                .errorDetail(errorDetail)
                .build();
    }
}
