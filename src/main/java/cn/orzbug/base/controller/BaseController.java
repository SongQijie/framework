package cn.orzbug.base.controller;


import cn.orzbug.base.entity.BaseEntity;
import cn.orzbug.base.entity.PageInfo;
import cn.orzbug.base.entity.ResponseResultEntity;
import cn.orzbug.base.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: SQJ
 * @data: 2018/6/29 10:20
 * @version:
 */
public class BaseController<T extends BaseEntity<I>, I extends Serializable> {

    @Autowired
    private ResponseCode responseCode;

    public Map<String, Object> getResult(int code, Object o) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", code);
        result.put("data", o);
        result.put("success", code == 200 ? true : false);
        result.put("msg", code == 200 ? "成功" : responseCode.getResponseMsg(code));
        return result;
    }

    public ResponseResultEntity response(Page<T> o) {
        List<T> entity = o.getContent();
        PageInfo pageInfo = new PageInfo();
        pageInfo.setTotalElements(o.getTotalElements());
        pageInfo.setTotalPages(o.getTotalPages());
        pageInfo.setNumberOfElements(o.getNumberOfElements());
        pageInfo.setSize(o.getSize());
        pageInfo.setSort("createTime");
        return new ResponseResultEntity(200, "成功", entity, true, pageInfo);
    }

}
