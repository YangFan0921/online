package com.graduation.config;

import com.graduation.service.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.annotation.Resource;

@Configuration
//启动Spring Security的权限管理功能
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    UserDetailsServiceImpl userDetailsService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()    //授权范围管理
                .antMatchers(   //"/index_student.html",
                        "/js/*",
                        "/css/*",
                        "/img/**",
                        "/bower_components/**",
                        "/login.html",
                        "/register.html",
                        "/resetpassword.html",
                        "/register",
                        "/users/resetPasswordByPhone")     //放行的资源
                .permitAll()
                .anyRequest()       //其他路径
                .authenticated()    //需要进行登录
                .and()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .failureUrl("/login.html?error")
                .defaultSuccessUrl("/index.html")   //登录成功默认显示的页面
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html?logout")
                .and()
                .sessionManagement()  //开启会话管理
                .invalidSessionUrl("/login.html")
                .maximumSessions(1)  //允许同一个用户只允许创建一个会话
                .expiredUrl("/login.html")//会话过期处理
                ;
//                .maxSessionsPreventsLogin(true)   //禁止再次登录
        ;
    }
}
