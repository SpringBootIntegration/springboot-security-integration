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
package com.edurt.detail;

import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * CustomWebAuthenticationDetails <br/>
 * 描述 : CustomWebAuthenticationDetails <br/>
 * 作者 : qianmoQ <br/>
 * 版本 : 1.0 <br/>
 * 创建时间 : 2018-04-03 下午11:33 <br/>
 * 联系作者 : <a href="mailTo:shichengoooo@163.com">qianmoQ</a>
 */
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

    private String captcha; // 验证码
    private String type; // 用户类型

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        // 从请求中抽取对应的参数, 注意我们只有在用户自定义的AuthenticationProvider中调用的时候才会生效
        this.captcha = request.getParameter("captcha");
        this.type = request.getParameter("type");
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}