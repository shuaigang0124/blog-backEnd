package com.gsg.blog.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @Description: TODO
 * @Author shuaigang
 * @Date 2021/11/26 16:36
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetailsVO implements Serializable {

    private static final long serialVersionUID = 7486652017634926711L;

    private String id;

    private String userName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private String email;

    private String phone;

    private String avatar;

    private Integer sex;

    private String address;

}
