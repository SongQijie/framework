package cn.orzbug.base.config;


import cn.orzbug.base.entity.ResponseResultEntity;
import cn.orzbug.base.utils.RedisUtil;
import cn.orzbug.base.utils.ResponseCode;
import com.google.common.base.Strings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: SQJ
 * @data: 2018/6/29 10:06
 * @version:
 */
@Aspect
@Component
public class ResponseBodyAspect {

    @Autowired
    private ResponseCode responseCode;

    @Autowired
    private RedisUtil redisUtil;

    @Pointcut("@annotation(cn.orzbug.base.config.RestJsonResponse)")
    public void responsePointCut() {
    }

    @Around("responsePointCut() && @annotation(restJsonResponse)")
    public Object doAround(ProceedingJoinPoint joinPoint, RestJsonResponse restJsonResponse) throws Throwable {
        //验证登录信息
        //从cookie中获取登陆信息
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String username = request.getHeader("BW-U");
        String token = request.getHeader("BW-Token");
        if (restJsonResponse.auth().getKey() == 1) {
            if (!Strings.isNullOrEmpty(username) && !Strings.isNullOrEmpty(token)) {
                //需要验证token
                Object redisToken = redisUtil.get(username + "-token");
                if (redisToken == null || Strings.isNullOrEmpty(redisToken.toString()) || !token.equalsIgnoreCase(redisToken.toString())) {
                    return setResponseResultEntity(1002, null);
                }
            } else {
                return setResponseResultEntity(1003, null);
            }
        }

        //继续流程
        Object result = joinPoint.proceed();
        //处理返回值
        if (result != null && (result instanceof HashMap)) {
            HashMap<String, Object> resultMap = (HashMap<String, Object>) result;
            result = setResponseResultEntity((int) resultMap.get("code"), resultMap.get("data"));
        } else if (result != null && !(result instanceof ResponseResultEntity)) {
            if (result instanceof Collection<?> || result instanceof Map<?, ?>) {
                result = new ResponseResultEntity(result);
            } else {
                result = new ResponseResultEntity(result);
            }
        }
        return result;
    }

    private ResponseResultEntity setResponseResultEntity(int code, Object data) {
        ResponseResultEntity restResults = new ResponseResultEntity();
        restResults.setData(data);
        restResults.setCode(code);
        restResults.setMsg(responseCode.getResponseMsg(code));
        restResults.setSuccess(code == 200 ? true : false);
        return restResults;
    }
}
