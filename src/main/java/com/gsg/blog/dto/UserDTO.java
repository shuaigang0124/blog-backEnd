package com.gsg.blog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.gsg.blog.utils.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Description: TODO
 * @Author shuaigang
 * @Date 2021/8/24 15:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -4492358815972132776L;

    /**
     * 用户ID：GSG+年月日(6位)+6位计数器(每日重新计数)
     */
    private String id;

    /** 昵称 */
    @NotBlank(message = "请输入昵称!")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{2,8}$",message = "昵称为中文，长度必须在2-8位之间")
    private String userName;

    /** 密码 */
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^[A-Za-z0-9]{8,16}$", message = "密码为8-16个字符，可以是大小写字母和数字!")
    private String password;

    /** 生日 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 序列化
    private LocalDate birthday;

    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = "^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$", message = "邮箱格式错误!")
    private String email;

    @NotBlank(message = "手机号不能为空!")
    @Pattern(regexp = "0?(13|14|15|18|17)[0-9]{9}", message = "手机号格式错误!")
    private String phone;

    /** 头像 */
    private String avatar;

    private String role;

    private Integer sex;

    /** 地址 */
    @NotBlank(message = "地址不能为空")
    private String address;

    /**
     * 创建时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 序列化
    private LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 序列化
    private LocalDateTime gmtModified;

    private String[] ids;

    /** 分页参数 */
    private Page page;

    /** 旧密码 */
    private String oldPassword;

    /** 新密码 */
    private String newPassword;

    /** 模糊查询字段 */
    private String fuzzySearch;
}
