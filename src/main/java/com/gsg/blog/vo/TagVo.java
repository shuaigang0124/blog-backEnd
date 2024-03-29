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
     * 标签id
     */
    private Integer id;

    /**
     * 标签颜色
     */
    private String color;

    /**
     * 标签名称
     */
    private String name;
}
