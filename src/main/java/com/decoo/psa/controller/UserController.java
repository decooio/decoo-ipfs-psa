package com.decoo.psa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.decoo.psa.dao.ApiKeyDao;
import com.decoo.psa.dao.UserDao;
import com.decoo.psa.domain.ApiKey;
import com.decoo.psa.domain.CommonResponse;
import com.decoo.psa.domain.User;
import com.decoo.psa.domain.UserCreateDTO;
import com.decoo.psa.utils.CommonUtils;
import com.decoo.psa.utils.JWTUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ApiKeyDao keyDao;

    @Transactional
    @PostMapping("/signUp")
    public ApiKey signUp(@RequestBody UserCreateDTO c) {
        if (userDao.selectCount(new LambdaQueryWrapper<User>().eq(User::getEmail, c.getEmail())) > 0) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT);
        }
        Date date = new Date();
        User user = new User();
        BeanUtils.copyProperties(c, user);
        user.setLastUpdateAt(date)
                .setCreateAt(date)
                .setStatus(User.UserStatus.CONFIRMED.getCode())
                .setRole(User.UserRole.USER.getCode())
                .setPassword(c.getPassword());
        userDao.insert(user);
        ApiKey apiKey = new ApiKey()
                .setUserId(user.getId())
                .setCreateAt(date)
                .setLastUpdateAt(date)
                .setStatus(ApiKey.KeyStatus.VALID.getCode())
                .setApiSecret(CommonUtils.getRandomSecret())
                .setApiKey(CommonUtils.getDefaultKeyByUserId(user.getId()));
        apiKey.setJwt(JWTUtils.createJwtByUser(apiKey));
        keyDao.insert(apiKey);
        return apiKey;
    }
}
