package com.decoo.psa.service.auth;

import com.decoo.psa.domain.Api;
import com.decoo.psa.domain.ApiKey;
import com.decoo.psa.domain.User;

import java.util.List;

public interface AuthService {

    public User loginUser();

    public ApiKey apiKey();

    public List<Api> apiList();

    public Long uid();

}
