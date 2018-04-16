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
package com.edurt.permission;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;

/**
 * CustomPermission <br/>
 * 描述 : CustomPermission <br/>
 * 作者 : qianmoQ <br/>
 * 版本 : 1.0 <br/>
 * 创建时间 : 2018-04-08 下午11:30 <br/>
 * 联系作者 : <a href="mailTo:shichengoooo@163.com">qianmoQ</a>
 */
@Configuration
public class CustomPermission implements PermissionEvaluator {

    private static final GrantedAuthority ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");

    @Override
    public boolean hasPermission(Authentication authentication, Object data, Object permission) {
        System.out.println("ID = " + data);
        if (data instanceof Long && permission instanceof String) {
            // 如果是管理员权限, 直接通过
            if (authentication.getAuthorities().contains(ADMIN)) {
                return true;
            }
            // 如果是非管理员权限, 是否是当前用户自己
            return isSelf(authentication, data);
        }
        throw new UnsupportedOperationException("第一个参数必须是long类型, 第二个参数必须是string类型");
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }

    private boolean isSelf(Authentication authentication, Object source) {
        Long primary = (Long) source;
        return primary > 1 && isSelfUser(authentication);
    }

    private boolean isSelfUser(Authentication authentication) {
        return authentication.getName().equals("admin");
    }

}