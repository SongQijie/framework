package cn.orzbug;


import cn.orzbug.base.utils.HttpClientUtils;
import cn.orzbug.base.utils.Md5Utils;
import net.sf.json.JSONObject;
import sun.jvm.hotspot.utilities.HashtableEntry;

import java.security.MessageDigest;
import java.util.*;

public class Test {


    public static void main(String[] args) {
        String wxCode = "061f4KgF1xvB670My3jF1emSgF1f4Kg8";
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String,String> requestUrlParam = new HashMap<String,String>();
        requestUrlParam.put("appid", "wx80422f57b207b545");
        requestUrlParam.put("secret", "8f44bab4d3d70a20faf5745603e77fa3");
        requestUrlParam.put("js_code", wxCode);
        requestUrlParam.put("grant_type", "authorization_code");
        //实际环境，使用返回：
        String resp = HttpClientUtils.doGet(requestUrl,requestUrlParam);
        //开发现模拟因为resp是一样的
//        String resp="{\"session_key\":\"jonRj1OX0ejqcEyEB+I38w==\",\"openid\":\"o4wCX5DZI8vjZaCYeYgVRx_xpF8g\"}";
        System.out.println(resp);

        String resp2 = HttpClientUtils.doGet(requestUrl,requestUrlParam);
        System.out.println(resp2);
    }

    
}
