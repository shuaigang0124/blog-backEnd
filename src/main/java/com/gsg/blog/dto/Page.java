package com.gsg.blog.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * TODO
 *
 * @author shuaigang
 * @date 2023/2/22 13:46
 */
@Data
@Accessors(chain = true)
public class Page implements Serializable {

    private static final long serialVersionUID = 3322425777625149931L;

    /**
     * 分页参数---当前页
     */
    private Integer pageNum;

    /**
     * 分页参数---当前显示数量
     */
    private Integer pageSize;

}
