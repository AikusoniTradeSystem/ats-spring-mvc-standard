package io.github.aikusonitradesystem.mvcstandard.model.view;


import io.github.aikusonitradesystem.core.common.BaseErrorCode;
import io.github.aikusonitradesystem.core.constants.ErrorCode;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

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

    public ResponseEntity<ATSResponseBody<T>> toResponseEntity() {
        return ResponseEntity.status(errorCode.getStatusCode()).body(this);
    }

    public ResponseEntity<ATSResponseBody<T>> toResponseEntity(int httpStatusCode) {
        return ResponseEntity.status(httpStatusCode).body(this);
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
