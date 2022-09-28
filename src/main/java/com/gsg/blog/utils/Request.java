package com.gsg.blog.utils;

import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * @Description 统一的请求实体
 * @Author shuaigang
 * @Date 下午 01:53:01 2021/8/13
 * @Version 1.0
 * @Param
 * @return 
 **/
@Data
public class Request<T> implements Serializable {

    private static final long serialVersionUID = -4559972876757237859L;

    @Valid
    private T customData;
}
