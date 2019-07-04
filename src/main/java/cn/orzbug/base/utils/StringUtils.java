package cn.orzbug.base.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: SQJ
 * @data: 2018/4/9 13:45
 * @version:
 */
public class StringUtils {

    private final static Logger logger = LoggerFactory.getLogger(StringUtils.class);

    private static List<String> NAME = new ArrayList<>();

    static {
        NAME.add("P");
        NAME.add("PW");
        NAME.add("SU");
        NAME.add("AU");
        NAME.add("NU");
        NAME.add("DU");
        NAME.add("O");
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取验证码
     *
     * @return
     */
    public static String getVcode() {
        StringBuilder vcode = new StringBuilder();
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 4; i++) {
            vcode.append(random.nextInt(10));
        }
        logger.info("create vcode , vcode is " + vcode);
        return vcode.toString();
    }

    public static String getUid(RedisUtil redisUtil){
       String uid =  redisUtil.incr("UID").toString();
       return uid;
    }


    public static String getOrderIdByUUId(String machineId) {
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }

    public static String getDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(new Date());
    }

    public static String getTimeDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        return df.format(new Date());
    }

    /**
     * 获取id
     *
     * @param type type如上
     * @return
     */
    public static String getId(String type) {
        if (!NAME.contains(type)) {
            return null;
        }
        return getDate() + Symbol.UNDERLINE + getOrderIdByUUId(type);
    }


    public static String getToken() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    public static String transferTel(String tel) {
        if (tel.length() == 11) {
            StringBuilder telbuilder = new StringBuilder(tel);
            telbuilder.replace(4, 8, "****");
            return telbuilder.toString();
        }
        return null;
    }

    public static boolean isNullOrEmpty(String string) {
        if (string == null || "".equals(string)) {
            return true;
        }
        return false;
    }


    public static void main(String[] args) {

//        System.out.println(getOrderId((RedisUtil) SpringUtil.getBean("redisUtil" )));

    }

    public static String getFolder(String root) {
        String date = DateUtils.getTheDate();
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        return root + Symbol.SLASH + date + Symbol.SLASH + "00" + hour + Symbol.SLASH;
    }

    public static String getOrderId(RedisUtil redisUtil) {
        StringBuffer orderId = new StringBuffer("wx" + getDate());
        Long id = redisUtil.incr("getOrderId");
        String hex = Long.toHexString(id).toUpperCase();
        if (hex.length() < 7) {
            int add = 6 - hex.length();
            for (int i = 0; i < add; i++) {
                orderId.append("0");
            }
            orderId.append(hex);
        }
        return orderId.toString();
    }

//    public static String getUid(RedisUtil redisUtil){
//
//    }



}