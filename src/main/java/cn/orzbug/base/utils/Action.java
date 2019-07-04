package cn.orzbug.base.utils;

/**
 * 接口注解，是否进行校验以及返回json信息
 *
 * @author: SQJ
 * @data: 2018/5/4 09:49
 * @version:
 */
public enum Action {

    CHECKTOKENSKIP(0,"略过token校验"),

    CHECKTOKEN(1, "验证token信息"),

    RETURNJSON(2, "返回json格式信息"),

    CHECKANDRETURN(3, "校验加返回");

    Action(int key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    private int key;
    private String desc;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
