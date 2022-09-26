package com.gsg.blogbackend.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author gaoshenggang
 * @date  2021/9/29 17:27
 */
@Data
@Accessors(chain = true)
public class RequestDTO implements Serializable {
    private static final long serialVersionUID = -3710267341380836279L;

    private String userId;

    private String qrCodeTempToken;
}
