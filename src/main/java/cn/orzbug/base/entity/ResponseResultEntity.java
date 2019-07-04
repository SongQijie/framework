package cn.orzbug.base.entity;

/**
 * @author: SQJ
 * @data: 2018/6/29 10:08
 * @version:
 */
public class ResponseResultEntity {

    private int code = 200;
    private PageInfo pageInfo;
    private String msg = "成功";
    private Object data = null;
    private Boolean success = true;

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public ResponseResultEntity(Object data){
        this.data =data;
    }

    public ResponseResultEntity(){

    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public ResponseResultEntity(int code, String msg, Object data, Boolean success) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.success = success;
    }

    public ResponseResultEntity(int code, String msg, Object data, Boolean success,PageInfo pageInfo) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.success = success;
        this.pageInfo = pageInfo;
    }

    public ResponseResultEntity(int code,  Object data) {
        this.code = code;
        this.data = data;
    }
}
