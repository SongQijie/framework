package cn.orzbug.base.specification;

import lombok.Builder;
import lombok.Data;

/**
 * @author: SQJ
 * @data: 2018/4/7 15:33
 * @version:
 */
@Builder
@Data
public class QueryBean {

    private String fileName;
    private Object fileValue;
    private Operator operator;
    private QueryBean relevance;
}
