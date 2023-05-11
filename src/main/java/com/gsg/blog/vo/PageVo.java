package com.gsg.blog.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * TODO
 *
 * @author shuaigang
 * @date 2023/3/3 14:41
 */
@Data
@Accessors(chain = true)
public class PageVo {

    /**
     * 分页参数---当前页
     */
    private Integer pageSize;

    /**
     * 分页参数---当前显示数量
     */
    private Integer pageNum;
    /**
     * 总数
     */
    private Integer total;

    /**
     * 分页参数---总页数
     */
    private Integer pages;

    private Object list;
}
