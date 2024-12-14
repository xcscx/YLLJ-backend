package com.itegg.yllj_backend.aop.aspect;

import cn.hutool.core.util.ObjectUtil;
import com.itegg.yllj_backend.aop.annotation.AuthCheck;
import com.itegg.yllj_backend.exception.BusinessException;
import com.itegg.yllj_backend.exception.ErrorCode;
import com.itegg.yllj_backend.model.entity.User;
import com.itegg.yllj_backend.model.enums.UserRoleEnum;
import com.itegg.yllj_backend.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    /**
     * 执行拦截
     * @param joinPoint 切入点
     * @param authCheck 权限校验注解
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 当前登录用户
        User loginUser = userService.getLoginUser(request);
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByCode(mustRole);
        // 不需要权限,放行
        if(ObjectUtil.isNull(mustRoleEnum)) {
            return joinPoint.proceed();
        }
        // 获取当前用户具有的权限
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByCode(loginUser.getUserRole());
        // 没有权限,拒绝
        if(ObjectUtil.isNull(userRoleEnum)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 要求必须要管理员权限
        if(UserRoleEnum.ADMIN.equals(mustRoleEnum) && !UserRoleEnum.ADMIN.equals(userRoleEnum)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 通过权限校验
        return joinPoint.proceed();
    }
}
