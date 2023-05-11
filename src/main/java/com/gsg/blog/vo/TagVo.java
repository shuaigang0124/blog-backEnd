package com.gsg.blog.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * TODO
 *
 * @author shuaigang
 * @date 2023/5/9 11:24
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagVo implements Serializable {

    private static final long serialVersionUID = 9194482394615548389L;

    /**
     * 标签类型，0-info;1-success;2-warning;3-danger;4-无指定类型
     */
    private Integer type;

    /**
     * 标签名称
     */
    private String name;
}
