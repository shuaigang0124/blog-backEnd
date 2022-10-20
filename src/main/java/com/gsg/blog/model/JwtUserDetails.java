package com.gsg.blog.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @Description: 用户类
 * @Author shuaigang
 * @Date 2021/9/29 11:48
 */
@Data
@NoArgsConstructor
public class JwtUserDetails implements UserDetails { // 实现UserDeails接口

    // 用户id
    private String userId;

    // 用户名
    private String username;
    // 密码
    private String password;
    //角色
    private String role;

    public JwtUserDetails(String userId, String username, String password, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
