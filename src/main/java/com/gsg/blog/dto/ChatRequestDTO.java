package com.gsg.blog.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author shuaigang
 * @since JDK 11.0.10
 * ClassName: ChatResponseVo
 * date: 2022/8/31 17:57
 */

@Data
@Component
@Accessors(chain = true)
public class ChatRequestDTO implements Serializable {

    private static final long serialVersionUID = -4220055767381301366L;

    private String version =  "V1";

//    private String encryptionType = "base64";

    private String data;

    /**
     * 服务器成功返回用户请求的数据
     * @param data 数据
     */
    public static ChatRequestDTO ok(String data){
        return new ChatRequestDTO().init(data);
    }

    public ChatRequestDTO init(String data) {
        return new ChatRequestDTO().setData(data);
    }
}
