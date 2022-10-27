package com.gsg.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询通用返回值VO
 * @author gaoshenggang
 * @date  2022/10/27 13:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PageResponseVO<T> implements Serializable {

    private static final long serialVersionUID = 8694307543961463267L;
    /**
     * @Description 查询数据总条数
     **/
    private long totalCount;

    /**
     * @Description 当前页码
     **/
    private Integer currentPage;

    /**
     * @Description 分页查询当前页返回数据量
     **/
    private Integer count;

    /**
     * @Description 返回结果集
     **/
    private List<T> resultList;
}
