# wechat-login

一个配合小程序登录的前后台组件（Token形式）

## API说明

> 你需要将WechatTemplate注入到项目中，对于SpringBoot项目而言，你只需要将这个项目的包扫描进入即可。

关于所有API你都可以在com.github.unclecatmyself.wechat.WechatTemplate中找到。

* public Map<String,String> getOpenId(String code)

获取OpenId，其将返回一个Map

| type | 内容 | 备注|
| null | null | 返回值为空|
| errCode | 错误码 |      |
| session_key | session_key值|  |
| openid | openid值|  |

* public String granToken(String userid)

生成Token

* public boolean verifyToken(String token)

校验Token

## 配置说明

请在项目中的application.yml中做配置,均为必填项

```java
wechat:
  wxurl: https://api.weixin.qq.com/sns/jscode2session?
  appid: wxabc2f8828c8e0048
  appsecret: cec2412a3af99200f4573c337715329e
  granttype: authorization_code
  redis:
    expire: 7200
    wxtoken: wx_token_%s
spring:
  redis:
    port: 6379
    host: 192.168.192.132
```

## Issues & Questions

https://github.com/UncleCatMySelf/wechat-login/issues

QQ Group：628793702

## About the author

![Image text](https://raw.githubusercontent.com/UncleCatMySelf/img-myself/master/img/%E5%85%AC%E4%BC%97%E5%8F%B7.png)


