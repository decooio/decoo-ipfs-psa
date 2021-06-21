package com.decoo.psa.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "spring.http-client.pool")
@Data
public class HttpClientPoolConfig {

    private int maxTotalConnect ;

    private int maxConnectPerRoute ;

    private int connectTimeout;

    private int readTimeout;

    private String charset = "UTF-8";

    private int retryTimes;

    private int connectionRequestTimeout;

    private Map<String,Integer> keepAliveTargetHost;

    private int keepAliveTime;

}

