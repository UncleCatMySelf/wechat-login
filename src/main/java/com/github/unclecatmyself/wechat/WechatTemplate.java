package com.github.unclecatmyself.wechat;

import com.alibaba.fastjson.JSON;
import com.github.unclecatmyself.config.RedisComponent;
import com.github.unclecatmyself.config.WeChatComponent;
import com.github.unclecatmyself.utils.HttpServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by MySelf on 2018/11/19.
 */
@Slf4j
@Component
public class WechatTemplate {

    @Autowired
    private RedisComponent redisComponent;

    @Autowired
    private WeChatComponent weChatComponent;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 获取OpenId
     * @param code 微信code
     * @return {@link Map}
     */
    public Map<String,String> getOpenId(String code){
        Map<String,String> back = new HashMap<>();
        Map<String,String> wxResult = new HashMap<>();
        String wxLoginUrl = weChatComponent.url(code);
        String result = HttpServiceUtils.sendGet(wxLoginUrl);
        if (result.isEmpty()){
            back.put("null","null");
        }else{
            wxResult = (Map) JSON.parse(result);
            if (wxResult.containsKey("errCode")){
                //存在错误码
                back.put("errCode",wxResult.get("errCode"));
            }else{
                //不存在错误码
                String session_key = wxResult.get("session_key");
                back.put("session_key",session_key);
                log.info("【微信Token】session_key:"+session_key);
                String openid = wxResult.get("openid");
                back.put("openid",openid);
            }
        }
        return back;
    }

    /**
     * 生成Token
     * @param userid 用户id
     * @return {@link String}
     */
    public String granToken(String userid){
        return saveToRedis(userid);
    }

    /**
     * 获取Token并存放到redis中
     * @param userid 用户id
     * @return {@link String}
     */
    private String saveToRedis(String userid) {
        String token = UUID.randomUUID().toString();
        Integer expire = redisComponent.getExpire();
        redisTemplate.opsForValue().set(String.format(redisComponent.getWxtoken(),token),userid,expire, TimeUnit.SECONDS);
        return token;
    }

    /**
     * 校验是否存在用户信息
     * @param token 唯一值
     * @return {@link Boolean}
     */
    public boolean verifyToken(String token){
        String tokenValue = redisTemplate.opsForValue().get(String.format(redisComponent.getWxtoken(),token));
        if (tokenValue.isEmpty()){
            return false;
        }
        return true;
    }

}
