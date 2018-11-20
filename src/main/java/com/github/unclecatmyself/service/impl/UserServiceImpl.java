package com.github.unclecatmyself.service.impl;

import com.github.unclecatmyself.service.UserService;
import com.github.unclecatmyself.wechat.WechatTemplate;
import com.myself.winter.utils.ResultVOUtil;
import com.myself.winter.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by MySelf on 2018/11/19.
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private WechatTemplate wechatTemplate;

    @Autowired(required = true)
    private ResultVOUtil resultVOUtil;

    @Override
    public ResultVo getToken(String code) {
        Map<String,String> result = wechatTemplate.getOpenId(code);
        if (result.containsKey("null")){
            return resultVOUtil.error(555,"返回值为空");
        }else if(result.containsKey("errCode")){
            return resultVOUtil.error(666,"存在错误码，内容："+result.get("errCode"));
        }else{
            String sessionKey = result.get("session_key");
            String openid = result.get("openid");
            log.info("openid="+openid+"--sessionKey="+sessionKey);
            //与存在用户的openid信息进行对比，返回用户id，不存在则注册用户
            String userid = "WX_10agg";//模拟获取到的用户id
            String token = wechatTemplate.granToken(userid);
            return resultVOUtil.success(token);
        }
    }

    @Override
    public ResultVo verifyToken(String token) {
        return resultVOUtil.success(wechatTemplate.verifyToken(token));
    }
}
