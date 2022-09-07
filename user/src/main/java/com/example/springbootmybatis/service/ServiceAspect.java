package com.example.springbootmybatis.service;

import com.example.springbootmybatis.dao.DataPermissionDao;
import com.example.springbootmybatis.pojo.*;
import com.example.springbootmybatis.util.UserInfoUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@Aspect
public class ServiceAspect {

    private static Logger logger = LoggerFactory.getLogger(ServiceAspect.class);

    @Autowired
    private DataPermissionDao dataPermissionDao;

    private static final String RESOURCE_USER = "user";

    private static final String RESOURCE_ROLE = "role";

    private static final String RESOURCE_PERMISSION = "permission";

    private static final List methodHandleList = Arrays.asList(
            "UserServiceImpl.deleteUserById(..)",
            "UserServiceImpl.queryUserById(..)",
            "UserServiceImpl.updateUser(..)",

            "RoleServiceImpl.queryRoleById(..)",
            "RoleServiceImpl.deleteRoleById(..)",
            "RoleServiceImpl.updateRole(..)",

            "PermissionServiceImpl.queryPermissionById(..)",
            "PermissionServiceImpl.deletePermissionById(..)",
            "PermissionServiceImpl.updatePermission(..)"
    );

    @Pointcut("execution(* com.example.springbootmybatis.service.UserService.*(..))")
    public void pointCutUser(){
    }

    @Pointcut("execution(* com.example.springbootmybatis.service.RoleService.*(..))")
    public void pointCutRole(){
    }

    @Pointcut("execution(* com.example.springbootmybatis.service.PermissionService.*(..))")
    public void pointCutPermission(){
    }

    @Around("pointCutUser()")
    public Object aroundUser(ProceedingJoinPoint joinPoint) throws Throwable {
        return doAround(joinPoint, "id", RESOURCE_USER);
    }

    @Around("pointCutRole()")
    public Object aroundRole(ProceedingJoinPoint joinPoint) throws Throwable {
        return doAround(joinPoint, "id",RESOURCE_ROLE);
    }

    @Around("pointCutPermission()")
    public Object aroundPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        return doAround(joinPoint, "id",RESOURCE_PERMISSION);
    }

    private Object doAround(ProceedingJoinPoint joinPoint, String idType, String resource) throws Throwable {
        JsonRespBo respBo = new JsonRespBo();
        Object[] args = joinPoint.getArgs();
        String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        String fullName = joinPoint.getSignature().toShortString();
        System.out.println("args:"+ args[0].toString() + " argNames:" + argNames.toString() + " fullName:" + fullName);

        if (!methodHandleList.contains(fullName)) {
            return joinPoint.proceed();
        }

        Integer id = getId(idType, argNames, args);
        try {
            logger.debug("校验用户授权信息:id={},resource={}",id,resource);
            if (hasAccess(id, resource)) {
                logger.debug("用户授权校验通过:id={},resource={}",id,resource);
                return joinPoint.proceed();
            }
        } catch (Exception e) {
            logger.error("授权用户校验错误", e);
            respBo.setCode("0");
            respBo.setMessage("授权用户校验错误");
            return respBo;
        }
        respBo.setCode("0");
        respBo.setMessage("用户无数据权限访问！");
        return respBo;
    }

    private boolean hasAccess(Integer id, String resource) {

        List roleList = UserInfoUtils.getRole();
        if (Objects.nonNull(roleList) &&
                roleList.size()>0 &&
                roleList.contains("超级管理员")) {
            return true;
        }

        Integer userId = UserInfoUtils.getUserId();
        List<Data_Permission> dataPerm= dataPermissionDao.getAuthorizedData(userId, resource, id);   //UserInfoUtils.getDataPermission(userId, id, resource);
        if (Objects.nonNull(dataPerm) && dataPerm.size()>0) {
            return true;
        }
        return false;
    }

    private Integer getId(String idType, String[] argNames, Object[] args){
        Integer id = null;
        for (int i = 0; i < argNames.length; i++) {
            logger.debug("用户id获取,argNames[i]={}",argNames[i]);
            if (Objects.equals(idType, argNames[i])) {
                String idStr = args[i].toString();
                if (idStr != null) {
                    id = Integer.valueOf(idStr);
                }
            }
            if (Objects.equals(RESOURCE_USER, argNames[i])) {
                User user = (User) args[i];
                id = user.getId();
                break;
            }
            if (Objects.equals(RESOURCE_ROLE, argNames[i])) {
                Role role = (Role) args[i];
                id = role.getId();
                break;
            }
            if (Objects.equals(RESOURCE_PERMISSION, argNames[i])) {
                Permission permission = (Permission) args[i];
                id = permission.getId();
                break;
            }
        }
        return id;
    }
}
