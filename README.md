## 先了解下SSO

对于单点登陆浅显一点的说就是两种，一种web端的基于Cookie、另一种是跨端的基于Token，一般想要做的都优先做Token吧，个人建议，因为后期扩展也方便哦。

小程序也是呢，做成token的形式是较好的。

## 流程图

    1、启动服务
    2、小程序初次加载app.js校验token，使用code去换取token
    3、检测User信息是否存在code，不存在则注册新用户，最后返回对应用户Id
    4、将随机Token与UserId一起存入Redis中
    5、返回Token信息，小程序存于缓存中，作为api请求的认证凭证
    
这个流程思路对什么后台语言都是通用的。

## 具体实现

> 本文的环境主要是做SpringBoot的，所有对于其他框架可能没有很好的兼容。

直接上代码弄起来吧！

首先是开源的话，我们需要确定某些东西是一定要配置的，不能写死。那么我写了两个Config类来获取application.yml中的数据，不同用户可以配置他们的参数。

    wechat:
      wxurl: https://api.weixin.qq.com/sns/jscode2session?
      appid: wxabc2f8828c8e0049
      appsecret: cec2412a3af99200f4573c337715329a
      granttype: authorization_code
      redis:
        expire: 7200
        wxtoken: wx_token_%s
    spring:
      redis:
        port: 6379
        host: 192.168.192.132

我这边了以上的参数作为组件中的可配置，其实部分可以作为默认的，不过暂时没有改了，如果你像要使用就是暂时都是必选的。

### 项目目录

config包中的就是对配置参数的读取。

utils包是存放一个Http的请求工具。

最核心的就是我们的WechatTemplate类了。

根据业务，我们需要以下几个方法：

**根据小程序传递来的code获取openid**

```java
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
```

**根据openid，我们可以和数据库对接得到用户id并生成自己Token**

```java
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
```

**还有校验Token，是否存在**

```java
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
```

## Maven包

接着打包发到Maven中央仓库中，生成自己的maven包

```
<dependency>
  <groupId>com.github.UncleCatMySelf</groupId>
  <artifactId>wechat-login</artifactId>
  <version>2.1.0</version>
</dependency>
```

## 如何使用？

我在Github项目中，做了Demo演示。

我们仅需在Service中调用，并使用对应方法即可。

```java
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
```

> tip:记得添加扫描包路径，@ComponentScan({"com.github.unclecatmyself"})

