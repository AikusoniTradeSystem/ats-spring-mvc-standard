package io.github.aikusonitradesystem.mvcstandard.advice;

import io.github.aikusonitradesystem.core.constants.ErrorCode;
import io.github.aikusonitradesystem.core.exception.ATSRuntimeException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Aspect
@Component
public class CheckRoleAdvice implements Ordered {
    @Override
    public int getOrder() {
        return 1;
    }

    @Before("@annotation(io.github.aikusonitradesystem.mvcstandard.advice.CheckRole)")
    public void checkRole(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new ATSRuntimeException(ErrorCode.FORBIDDEN, "RCA-000001", "RequestAttributes가 존재하지 않습니다.");
        }

        HttpServletRequest request = attributes.getRequest();
        String roles = request.getHeader("X-Roles");

        if (roles == null) {
            throw new ATSRuntimeException(ErrorCode.FORBIDDEN, "RCA-000002", "권한이 설정되지 않은 계정으로 접근 했습니다.");
        }

        Method method = getMethodFromJoinPoint(joinPoint);
        CheckRole checkRole = method.getAnnotation(CheckRole.class);
        if (checkRole == null) {
            throw new ATSRuntimeException(ErrorCode.INTERNAL_SERVER_ERROR, "RCA-000004", "서버 오류로 인해 권한 체크에 실패했습니다. 관리자에게 문의하세요.");
        }

        String requiredRole = checkRole.value();
        if (!roles.contains(requiredRole)) {
            throw new ATSRuntimeException(ErrorCode.FORBIDDEN, "RCA-000003", "권한이 없는 계정으로 접근 했습니다.");
        }
    }

    private Method getMethodFromJoinPoint(JoinPoint joinPoint) {
        var signature = joinPoint.getSignature();
        var methodSignature = (org.aspectj.lang.reflect.MethodSignature) signature;
        Class<?> targetClass = joinPoint.getTarget().getClass();

        Method method = null;
        try {
            Class<?>[] parameterTypes = methodSignature.getParameterTypes();
            method = targetClass.getMethod(methodSignature.getName(), parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new ATSRuntimeException(ErrorCode.INTERNAL_SERVER_ERROR, "RCA-000005", "서버 오류로 인해 권한 체크에 실패했습니다. 관리자에게 문의하세요.");
        }
        return method;
    }
}

