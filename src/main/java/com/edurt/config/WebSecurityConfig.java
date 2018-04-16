/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.edurt.config;

import com.edurt.detail.CustomAuthenticationDetailsSource;
import com.edurt.encoder.CustomPasswordEncoder;
import com.edurt.hander.CustomAccessDeniedHandler;
import com.edurt.hander.CustomAuthenticationFailHander;
import com.edurt.hander.CustomAuthenticationSuccessHandler;
import com.edurt.point.UnauthorizedEntryPoint;
import com.edurt.provider.CustomAuthenticationProvider;
import com.edurt.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * WebSecurityConfig <br/>
 * 描述 : WebSecurityConfig <br/>
 * 作者 : qianmoQ <br/>
 * 版本 : 1.0 <br/>
 * 创建时间 : 2018-03-15 下午3:18 <br/>
 * 联系作者 : <a href="mailTo:shichengoooo@163.com">qianmoQ</a>
 */
@Configuration
// 开启security访问授权
@EnableWebSecurity
// 开启security注解模式
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationFailHander customAuthenticationFailHander;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    private UnauthorizedEntryPoint unauthorizedEntryPoint;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomPasswordEncoder customPasswordEncoder;

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private CustomAuthenticationDetailsSource customAuthenticationDetailsSource;

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // AccessDeniedHandler仅适用于已通过身份验证的用户。未经身份验证的用户的默认行为是重定向到登录页面（或适用于正在使用的身份验证机制的任何内容）。
//        http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);
        http.csrf().disable();
//        http.exceptionHandling().accessDeniedPage("/403");
        http.exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint);
        // 允许直接访问/路径
        http.authorizeRequests().antMatchers("/").permitAll()
                // 使其支持跨域
//                .requestMatchers(CorsUtils :: isPreFlightRequest).permitAll()
                // 其他路径需要授权访问
                .anyRequest().authenticated()
                // 指定登录页面
                .and().formLogin().loginPage("/user/login")
                .successHandler(customAuthenticationSuccessHandler)
                // 指定登录失败跳转地址, 使用自定义错误信息
                .failureHandler(customAuthenticationFailHander)
                // 指定登录失败跳转地址
                .failureUrl("/user/login?error").permitAll()
                // 登录成功后的默认路径
                .defaultSuccessUrl("/").permitAll()
                // 使用自定义的AuthenticationDetailsSource
                .authenticationDetailsSource(customAuthenticationDetailsSource)
                // 退出登录后的默认路径
                .and().logout().logoutSuccessUrl("/user/login").permitAll();
    }

    @Value(value = "${ldap.urls}")
    private String ldapUrls;

    @Value(value = "${ldap.base.dn}")
    private String ldapBaseDn;

    @Value(value = "${ldap.username}")
    private String ldapUsername;

    @Value(value = "${ldap.password}")
    private String ldapPassword;

    @Value(value = "${ldap.user.dn.pattern}")
    private String ldapUserDnPattern;

    @Autowired
//    @ConfigurationProperties(prefix = "ldap")
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // 使用ldap进行授权登录
        auth.ldapAuthentication().contextSource()
                // ldap://localhost:389/dc=edurt,dc=com,uid=2
                .url(ldapUrls + ldapBaseDn)
                .managerDn(ldapUsername)
                .managerPassword(ldapPassword)
                .and()
                .userDnPatterns(ldapUserDnPattern);
        // 配置用户登录检索策略
//        auth.userDetailsService(userDetailsService())
//                // 配置密码策略
//                .passwordEncoder(passwordEncoder());
//        auth.authenticationProvider(customAuthenticationProvider);
//        auth.inMemoryAuthentication().withUser("user").password("123456")
//                .and().withUser("admin").password("123456");
//        auth.userDetailsService(customUserDetailsService).passwordEncoder(customPasswordEncoder);
    }

    @Bean
    public Md5PasswordEncoder passwordEncoder() {
        return new Md5PasswordEncoder();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        // 创建模拟用户
//        manager.createUser(User.withUsername("user").password("123456").roles("USER").build());
//        manager.createUser(User.withUsername("admin").password("123456").roles("ADMIN").build());
//        return manager;
//    }

}