package com.gsg.blog.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.List;

/**
 * TODO
 *
 * @author shuaigang
 * @date 2023/2/22 13:46
 */
@Data
@Accessors(chain = true)
public class CommonDto implements Serializable {

    private static final long serialVersionUID = 3322425777625149931L;

    /**
     * 分页参数---当前页
     */
    @TableField(exist = false)
    private Integer pageNum;

    /**
     * 分页参数---当前显示数量
     */
    @TableField(exist = false)
    private Integer pageSize;

    /**
     * ids
     */
    @TableField(exist = false)
    private List<String> ids;

    /**
     * 当前用户id
     */
    @TableField(exist = false)
    private String nowUserId;

}
