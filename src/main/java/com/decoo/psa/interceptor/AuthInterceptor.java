package com.decoo.psa.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.decoo.psa.constants.BaseConstants;
import com.decoo.psa.dao.ApiKeyDao;
import com.decoo.psa.dao.UserDao;
import com.decoo.psa.domain.ApiKey;
import com.decoo.psa.domain.CommonResponse;
import com.decoo.psa.domain.User;
import com.decoo.psa.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    public static final String AUTH_API_KEY = "DECOO_AUTH_API_KEY";

    public static final String AUTH_USER = "DECOO_AUTH_USER";

    public static final String AUTH_API_LIST = "DECOO_AUTH_API_LIST";

    @Autowired
    private UserDao userDao;

    @Autowired
    private ApiKeyDao apiKeyDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getServletPath().equals("/user/signUp")) {
            return true;
        }
        // support preflighted request
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS");
            response.setHeader("Access-Control-Max-Age", "86400");
            response.setHeader("Access-Control-Allow-Headers", "*");
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return false;
        }
        String value = request.getHeader(BaseConstants.USER_JWT_HEADER_KEY);
        if (!StringUtils.isEmpty(value) && value.startsWith(BaseConstants.USER_JWT_PREFIX)) {
            String token = value.replaceAll(BaseConstants.USER_JWT_PREFIX, "");
            String secretKey = JWTUtils.getSecretKeyByToken(token);
            if (!StringUtils.isEmpty(secretKey)) {
                ApiKey apiKey = apiKeyDao.selectOne(new LambdaQueryWrapper<ApiKey>().eq(ApiKey::getApiKey, secretKey));
                if (apiKey != null && JWTUtils.verifyToken(token, apiKey.getApiSecret())) {
                    User u = userDao.selectById(apiKey.getUserId());
                    if (User.UserStatus.CONFIRMED.getCode().equals(u.getStatus())) {
                        request.setAttribute(AUTH_USER, u);
                        request.setAttribute(AUTH_API_KEY, apiKey);
                        return true;
                    }
                }
            }
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(CommonResponse.needAuth().getMsg());
        return false;
    }

}
