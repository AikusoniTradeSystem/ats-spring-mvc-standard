package io.github.aikusonitradesystem.mvcstandard.advice;

import io.github.aikusonitradesystem.core.constants.ErrorCode;
import io.github.aikusonitradesystem.core.exception.ATSRuntimeException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

import static io.github.aikusonitradesystem.core.utils.MessageUtils.m;

@Slf4j
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
            throw new ATSRuntimeException(ErrorCode.FORBIDDEN, "RCA-000001", m("mvc.no_request_attributes"));
        }

        HttpServletRequest request = attributes.getRequest();
        String roles = request.getHeader("X-Roles");

        if (roles == null) {
            throw new ATSRuntimeException(ErrorCode.FORBIDDEN, "RCA-000002", m("mvc.no_roles"));
        }

        Method method = getMethodFromJoinPoint(joinPoint);
        CheckRole checkRole = method.getAnnotation(CheckRole.class);
        if (checkRole == null) {
            throw new ATSRuntimeException(ErrorCode.INTERNAL_SERVER_ERROR, "RCA-000004", m("mvc.failed_to_access_server_error"));
        }

        String requiredRole = checkRole.value();
        if (!roles.contains(requiredRole)) {
            throw new ATSRuntimeException(ErrorCode.FORBIDDEN, "RCA-000003", m("mvc.no_permission"));
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
            throw new ATSRuntimeException(ErrorCode.INTERNAL_SERVER_ERROR, "RCA-000005", m("mvc.failed_to_access_server_error"));
        }
        return method;
    }
}

