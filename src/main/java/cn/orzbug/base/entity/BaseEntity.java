package cn.orzbug.base.entity;

import cn.orzbug.base.specification.Operator;
import cn.orzbug.base.specification.QueryPath;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author: SQJ
 * @data: 2018/6/29 10:24
 * @version:
 */
@MappedSuperclass
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BaseEntity<I extends Serializable> {

    private static final int ENTITY_DELETE = 1;
    private static final int ENTITY_UNDELETE = 0;
    private static final String USER="admin";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private I id;

    @JsonIgnore
    private Timestamp createTime = null;

    @JsonIgnore
    private Timestamp modifyTime = new Timestamp(System.currentTimeMillis());

    @JsonIgnore
    private String createUser = USER;

    @Transient
    private String createStr;

    @Transient
    private String modifyStr;

    @JsonIgnore
    private Integer isDelete = ENTITY_UNDELETE;

    public I getId() {
        return id;
    }

    public void setId(I id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getCreateStr() {
        if (null != this.getCreateTime()) {
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.setCreateStr(sdf.format(this.getCreateTime()));
        }
        return createStr;
    }

    public void setCreateStr(String createStr) {
        this.createStr = createStr;
    }

    public String getModifyStr() {
        if (null != this.getModifyTime()) {
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.setModifyStr(sdf.format(this.getModifyTime()));
        }
        return modifyStr;
    }

    public void setModifyStr(String modifyStr) {
        this.modifyStr = modifyStr;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
}
