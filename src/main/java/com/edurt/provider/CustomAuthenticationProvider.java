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
package com.edurt.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * CustomAuthenticationProvider <br/>
 * 描述 : CustomAuthenticationProvider <br/>
 * 作者 : qianmoQ <br/>
 * 版本 : 1.0 <br/>
 * 创建时间 : 2018-04-03 下午11:18 <br/>
 * 联系作者 : <a href="mailTo:shichengoooo@163.com">qianmoQ</a>
 */
@Component(value = "customAuthenticationProvider")
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 抽取需要授权的验证信息
        final String username = authentication.getName();
        final String password = authentication.getCredentials().toString();
        // 使用数据库进行授权
        // dbService.findByUsernameAndPassword(username, password);
        if (username.equals("admin") && password.equals("123456")) {
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            // 这里可以忽略
            final UserDetails principal = new User(username, password, grantedAuthorities);
            final Authentication auth = new UsernamePasswordAuthenticationToken(principal, password, grantedAuthorities);
            return auth;
//            return new UsernamePasswordAuthenticationToken(username, password, grantedAuthorities);
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authencation) {
        return authencation.equals(UsernamePasswordAuthenticationToken.class);
    }

}