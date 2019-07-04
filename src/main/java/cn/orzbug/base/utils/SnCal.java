package cn.orzbug.base.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;


/**
 * @author: SQJ
 * @data: 2018/6/1 10:23
 * @version:
 */
public class SnCal {

    public static void main(String[] args) throws UnsupportedEncodingException,
            NoSuchAlgorithmException {
        SnCal snCal = new SnCal();
        Map paramsMap = new LinkedHashMap<String, String>();
//        paramsMap.put("address", "北京市海淀区上地十街10号");
        paramsMap.put("location", "35.658651,139.745415");
        paramsMap.put("output", "json");
        paramsMap.put("ak", "so5YHHgc3E0zK7lmNkDhqbvsLbWealsM");
        String paramsStr = snCal.toQueryString(paramsMap);
        String wholeStr = new String("/geocoder/v2/?" + paramsStr + "FVkvrmIM595pLQbAR8ClqQxteulgX5sO");
        String tempStr = URLEncoder.encode(wholeStr, "UTF-8");
        System.out.println(snCal.MD5(tempStr));
        String string = HttpClientUtils.doGet("http://api.map.baidu.com/geocoder/v2/?location=35.658651,139.745415&output=json&ak=so5YHHgc3E0zK7lmNkDhqbvsLbWealsM&sn=" + snCal.MD5(tempStr));
//        String string = HttpClientUtils.doGet("http://api.map.baidu.com/geocoder/v2/?address=北京市海淀区上地十街10号&output=json&ak=so5YHHgc3E0zK7lmNkDhqbvsLbWealsM&sn=" + snCal.MD5(tempStr));
        System.out.println(string);
    }


    /**
     * 对Map内所有value作utf8编码，拼接返回结果
     *
     * @param data
     * @return
     * @throws UnsupportedEncodingException
     */
    public String toQueryString(Map<?, ?> data)
            throws UnsupportedEncodingException {
        StringBuffer queryString = new StringBuffer();
        for (Entry<?, ?> pair : data.entrySet()) {
            queryString.append(pair.getKey() + "=");
            String ss[] = pair.getValue().toString().split(",");
            if (ss.length > 1) {
                for (String s : ss) {
                    queryString.append(URLEncoder.encode(s, "UTF-8") + ",");
                }
                queryString.deleteCharAt(queryString.length() - 1);
                queryString.append("&");
            } else {
                queryString.append(URLEncoder.encode((String) pair.getValue(),
                        "UTF-8") + "&");
            }
        }
        if (queryString.length() > 0) {
            queryString.deleteCharAt(queryString.length() - 1);
        }
        return queryString.toString();
    }

    /**
     * 来自stackoverflow的MD5计算方法，调用了MessageDigest库函数，并把byte数组结果转换成16进制
     *
     * @param md5
     * @return
     */
    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
                        .substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }


}
