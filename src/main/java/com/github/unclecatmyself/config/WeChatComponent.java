package com.github.unclecatmyself.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by MySelf on 2018/11/19.
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WeChatComponent {

    private String wxurl;

    private String appid;

    private String appsecret;

    private String granttype;

    public String url(String code){
        return getWxurl()+"appid="+getAppid()+"&secret="+getAppsecret()+"&js_code="+code+"&grant_type"+getGranttype();
    }

}
