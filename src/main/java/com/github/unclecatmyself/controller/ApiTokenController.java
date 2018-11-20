package com.github.unclecatmyself.controller;

import com.github.unclecatmyself.service.UserService;
import com.myself.winter.utils.ResultVOUtil;
import com.myself.winter.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by MySelf on 2018/11/19.
 */
@RestController
@RequestMapping("/api/v1/token")
public class ApiTokenController {

    @Autowired
    private UserService userService;

    @Autowired
    private ResultVOUtil resultVOUtil;

    @PostMapping("/get_token")
    @ResponseBody
    public ResultVo getToken(String code){
        if (code.isEmpty()){
            return resultVOUtil.error(555,"参数不能为空");
        }
        return userService.getToken(code);
    }

    @PostMapping("/verify")
    public ResultVo verify(String token){
        if (token.isEmpty()){
            return resultVOUtil.error(555,"参数不能为空");
        }
        return userService.verifyToken(token);
    }

    @PostMapping("/save_user_info")
    public ResultVo saveUserInfo(String token,String avatarUrl,String city,String country,Integer gender,String nickName,String province){
        if (token.isEmpty()){
            return resultVOUtil.error(555,"参数不能为空");
        }
        return resultVOUtil.success();
    }

}
