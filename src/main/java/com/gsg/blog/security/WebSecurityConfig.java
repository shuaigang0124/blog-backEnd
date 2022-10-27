package com.gsg.blog.security;

import com.gsg.blog.filter.AvalidatorFilter;
import com.gsg.blog.filter.JwtAuthenticationTokenFilter;
import com.gsg.blog.handler.JwtAccessDeniedHandler;
import com.gsg.blog.handler.JwtAuthenticationEntryPoint;
import com.gsg.blog.handler.MyAuthenticationFailureHandler;
import com.gsg.blog.handler.MyAuthenticationSuccessHandler;
import com.gsg.blog.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * @Description: security配置
 * @Author shuaigang
 * @Date 2021/9/29 16:04
 */
@Component
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    // extends WebSecurityConfigurerAdapter  即将弃用

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Resource
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Resource
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Resource
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private AvalidatorFilter avalidatorFilter;

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;


    /**
     * 白名单, 不做权限效验的url
     */
    private static final String[] AUTH_WHITELIST = {
//            "/gsg/authentication/login",
            "/gsg/authentication/form",
            "/gsg/authentication/logout",
            "/gsg/authentication/generateToken",

            "/gsg/msg/getMsg",
            "/gsg/msg/insertMsg",

//            "/gsg/atc/insertArticle",
            "/gsg/atc/getArticle",

            "/gsg/test"
    };

    @Bean
    @Primary
    UserDetailsService myDetailsService() {
        return new UserDetailServiceImpl();
    }

    /**
     * 注意这个方法是注入的
     * @param auth 参数
     * @throws Exception 最大异常抛出
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myDetailsService());
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 整理格式
        http.addFilterBefore(avalidatorFilter, UsernamePasswordAuthenticationFilter.class);
        // 配置自己的JWT验证过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        http.formLogin()
                // 登录页面
//                .loginPage("/gsg/authentication/Login")
                // 登录通过页面
                .loginProcessingUrl("/gsg/authentication/form")
                // 自定义认证成功处理器
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailureHandler)
                .and()
                // token的延续恶化那个方式不需要开启csrf防护
                .csrf().disable()
                // 自定义认证失败类
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                // 自定义权限不足处理类
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                // 设置无状态链接，即不创建Session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(HttpMethod.GET, "/**/*.*").permitAll()
                // 白名单URL
                .antMatchers(AUTH_WHITELIST).permitAll()
                // 指定路径下的资源需要验证了的用户才能访问
//                .antMatchers("/**/release").hasAnyRole("SUPER_ADMIN", "DEVELOP_ADMIN")


                // 配置允许匿名访问的路径
                .anyRequest()
                .authenticated();


        // 禁用缓存
        http.headers().cacheControl();
        return http.build();
    }

//    @Override
//    @Bean
//    public AuthenticationManager authenticationManager() throws Exception {
//        return super.authenticationManager();
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        // 整理格式
//        http.addFilterBefore(avalidatorFilter, UsernamePasswordAuthenticationFilter.class);
//        // 配置自己的JWT验证过滤器
//        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
//
//        http.formLogin()
//                // 登录页面
//                .loginPage("/gsg/authentication/Login")
//                // 登录通过页面
//                .loginProcessingUrl("/gsg/authentication/form")
//                // 自定义认证成功处理器
//                .successHandler(myAuthenticationSuccessHandler)
//                .failureHandler(myAuthenticationFailureHandler)
//                .and()
//                // token的延续恶化那个方式不需要开启csrf防护
//                .csrf().disable()
//                // 自定义认证失败类
//                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                // 自定义权限不足处理类
//                .accessDeniedHandler(jwtAccessDeniedHandler)
//                .and()
//                // 设置无状态链接，即不创建Session
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .antMatchers(HttpMethod.GET, "/**/*.*").permitAll()
//                // 白名单URL
//                .antMatchers(AUTH_WHITELIST).permitAll()
//                // 指定路径下的资源需要验证了的用户才能访问
////                .antMatchers("/**/release").hasAnyRole("SUPER_ADMIN", "DEVELOP_ADMIN")
//
//
//                // 配置允许匿名访问的路径
//                .anyRequest()
//                .authenticated();
//
//
//        // 禁用缓存
//        http.headers().cacheControl();
//    }
}
