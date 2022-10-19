package com.gsg.blog.dto;

import com.gsg.blog.model.User;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author shuaigang
 * @date 2022/10/18 9:12
 */
@Data
public class EsPage {
    private String keyword;
    private Long total;
    private Integer current = 1;
    private Integer pageSize = 10;
    private List<Object> records;
}
