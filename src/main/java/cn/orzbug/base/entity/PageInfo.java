package cn.orzbug.base.entity;

import lombok.Data;

/**
 * @author: SQJ
 * @data: 2018/7/25 10:39
 * @version:
 */
@Data
public class PageInfo {

    private long totalElements;
    private int totalPages;
    private int numberOfElements;
    private int size;
    private String sort;
}
