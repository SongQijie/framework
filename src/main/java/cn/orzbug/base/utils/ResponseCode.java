package cn.orzbug.base.utils;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: SQJ
 * @data: 2018/6/29 10:16
 * @version:
 */
@Component
public class ResponseCode {

    private static final Map<Integer, String> responseCode = new HashMap<>();


    public ResponseCode() {
    }

    @PostConstruct
    public void init() {
        responseCode.put(200, "成功");
        responseCode.put(1000, "未知原因错误，请联系管理员");
        responseCode.put(1001, "Cookie异常");
        responseCode.put(1002, "登陆信息异常，请重新登陆");
        responseCode.put(1003, "请求头不携带登陆信息，请检查请求");
        responseCode.put(1004, "用户名或密码错误，请重新输入");
        responseCode.put(1005, "获取验证码超过一小时限制，5次");
        responseCode.put(1006, "获取验证码超过一天限制，15次");
        responseCode.put(1007, "验证码错误");
        responseCode.put(1008, "changeType参数不合法");
        responseCode.put(1009, "文件上传为空，请检查重新上传");
        responseCode.put(1010, "参数description不在可用范围内，请确认");
        responseCode.put(1011, "上传阿里云oss出错，请联系管理员");
        responseCode.put(1012, "7天之内曾修改，无法再次修改");
        responseCode.put(1013, "一个人只能创建一个厂牌");
        responseCode.put(1014, "账号被注册，请直接登录");
        responseCode.put(1015, "粉丝数不足");
    }

    public String getResponseMsg(int code) {
        return responseCode.get(code);
    }
}
