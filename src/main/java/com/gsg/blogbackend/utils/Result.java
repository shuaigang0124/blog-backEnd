package com.gsg.blogbackend.utils;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
@Accessors(chain = true)
@ConfigurationProperties(prefix = "result")
public class Result<T> implements Serializable {

    private String version =  "V1";

    private String encryptionType = "base64";

    private String data;

    /**
     * 服务器成功返回用户请求的数据
     * @param data 数据
     */
    public static Result ok(String data){
        return new Result().init(data);
    }

    public Result init(String data) {
        return new Result().setData(data);
    }

}
