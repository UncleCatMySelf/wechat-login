package com.github.unclecatmyself.service;

import com.myself.winter.vo.ResultVo;

/**
 * Created by MySelf on 2018/11/19.
 */
public interface UserService {

    ResultVo getToken(String code);

    ResultVo verifyToken(String token);

}
