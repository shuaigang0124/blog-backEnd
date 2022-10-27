package com.gsg.blog.utils;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页参数
 * @author gaoshenggang
 * @date  2021/11/15 13:15
 */
@Data
public class Page implements Serializable {

    private static final long serialVersionUID = 4202762957394441811L;

    /**
     * 当前页面
     */
    private Integer index = 1;

    /**
     * 每页大小
     */
    private Integer size = 10;

    /**
     * 列表总条数
     */
    private Long total;

    /**
     * 排序字段
     */
    private String sort;

    /**
     * 排序方式 DESC ASC
     */
    private PageOrder order;

    public Page() {
    }

    public Page(Integer index, Integer size, String sort, PageOrder order) {
        super();
        this.index = index;
        this.size = size;
        this.sort = sort;
        this.order = order;
    }

    public Page(Integer index, Integer size) {
        if (index == null || index <= 1) {
            index = 1;
        }
        if (size == null) {
            size = 10;
        }

        this.index = index;
        this.size = size;
    }

    /**
     * 分页开始偏移量
     *
     * @return
     */
    public Integer offset() {
        return (index - 1) * size;
    }

    /**
     * 分页结束偏移量
     *
     * @return
     */
    public Integer endOffset() {
        return index * size;
    }

    @Override
    public String toString() {
        return "Page{" +
                "index=" + index +
                ", size=" + size +
                ", total=" + total +
                ", sort='" + sort + '\'' +
                ", order='" + order + '\'' +
                '}';
    }
}
