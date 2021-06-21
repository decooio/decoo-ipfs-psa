package com.decoo.psa.constants;


import com.decoo.psa.utils.CommonUtils;

import java.time.Duration;

public class BaseConstants {

    public static final String VERIFICATION_EMAIL = CommonUtils.getenv("VERIFICATION_EMAIL", "noreply@decoo.io");

    public static final String VERIFICATION_EMAIL_PASSWORD = CommonUtils.getenv("VERIFICATION_EMAIL_PASSWORD", "Zol21309");

    public static final String VERIFICATION_CACHE_KEY_PREFIX = "VERIFICATION::EMAIL::USER::";

    public static final String API_KEY_CACHE_PREFIX = "API::KEY::CACHE::";

    public static final String USER_INFO_CACHE_PREFIX = "USER::INFO::";

    public static final String API_CACHE_PREFIX = "API::CACHE::";

    public static final String USER_COOKIE_CACHE_PREFIX = "USER::COOKIE::";

    public static final String USER_COOKE_KEY = "DECOO_USER";

    public static final String USER_JWT_HEADER_KEY = "Authorization";

    public static final String USER_INFO_CACHE = "USER_INFO_CACHE";

    public static final String USER_JWT_PREFIX = "Bearer ";

    public static final Duration VERIFICATION_CODE_TIMEOUT = Duration.ofSeconds(60L * 5);

    public static final Duration LOGIN_CACHE_TIMEOUT = Duration.ofSeconds(60L * 30);

    public static final Duration AUTH_CACHE_TIMEOUT = Duration.ofSeconds(60L * 15);

    public static final String DECOO_IPFS_FILE_CACHE_PATH = "/ipfs/tmp/";

    public static final Long DEFAULT_USER_ID = -1L;

    public static final String DEFAULT_HOST = "decoo.io";
}
