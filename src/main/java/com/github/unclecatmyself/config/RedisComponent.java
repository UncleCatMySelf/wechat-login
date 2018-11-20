package com.github.unclecatmyself.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by MySelf on 2018/11/19.
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat.redis")
public class RedisComponent {

    private Integer expire;

    private String wxtoken;

}
