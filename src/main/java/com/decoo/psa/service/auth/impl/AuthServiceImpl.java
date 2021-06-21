package com.decoo.psa.service.auth.impl;

import com.decoo.psa.domain.Api;
import com.decoo.psa.domain.ApiKey;
import com.decoo.psa.domain.User;
import com.decoo.psa.interceptor.AuthInterceptor;
import com.decoo.psa.service.auth.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    
    @Override
    public User loginUser() {
        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Object user = request.getAttribute(AuthInterceptor.AUTH_USER);
        return user == null ? null : (User) user;
    }

    @Override
    public ApiKey apiKey() {
        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Object apiKey = request.getAttribute(AuthInterceptor.AUTH_API_KEY);
        return apiKey == null ? null : (ApiKey) apiKey;
    }

    @Override
    public List<Api> apiList() {
        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Object apiList = request.getAttribute(AuthInterceptor.AUTH_API_LIST);
        return apiList == null ? null : (List<Api>) apiList;
    }

    @Override
    public Long uid() {
        User u = loginUser();
        return u == null ? null : u.getId();
    }


}
