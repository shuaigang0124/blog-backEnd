package com.gsg.blog.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: TODO
 * @Author shuaigang
 * @Date 2021/8/24 15:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserVO implements Serializable {

    private static final long serialVersionUID = -4795815345523096774L;
    /**
     * 用户ID：GSG+年月日(6位)+6位计数器(每日重新计数)
     */
    private String id;

    /** 昵称 */
    private String userName;

    /** 生日 */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date birthday;

    private String email;

    private String phone;

    /** 头像 */
    private String avatar;

    private String role;

    private Integer sex;

    /** 地址 */
    private String address;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date gmtModified;


}
