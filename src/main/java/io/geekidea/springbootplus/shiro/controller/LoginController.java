/*
 * Copyright 2019-2029 geekidea(https://github.com/geekidea)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.geekidea.springbootplus.shiro.controller;

import io.geekidea.springbootplus.common.api.ApiResult;
import io.geekidea.springbootplus.shiro.param.LoginParam;
import io.geekidea.springbootplus.shiro.service.LoginService;
import io.geekidea.springbootplus.shiro.util.JwtTokenUtil;
import io.geekidea.springbootplus.system.vo.LoginSysUserTokenVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static io.geekidea.springbootplus.util.HttpServletRequestUtil.getRequest;

/**
 * 登陆控制器
 *
 * @author geekidea
 * @date 2019-09-28
 * @since 1.3.0.RELEASE
 **/
@Api("登陆控制器")
@Slf4j
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "登陆", notes = "系统用户登陆", response = LoginSysUserTokenVo.class)
    public ApiResult login(@RequestParam(value="username") String username,
                           @RequestParam(value="password")String password,
                           @RequestParam(value="verifyToken")String verifyToken,
                           @RequestParam(value="code")String code, HttpServletResponse response) throws Exception {
        LoginParam loginParam = new LoginParam();
        loginParam.setUsername(username);
        loginParam.setPassword(password);
        loginParam.setVerifyToken(verifyToken);
        loginParam.setCode(code);
        LoginSysUserTokenVo loginSysUserTokenVo = loginService.login(loginParam);
        // 设置token响应头
        response.setHeader(JwtTokenUtil.getTokenName(), loginSysUserTokenVo.getToken());
        HttpSession session = getRequest().getSession();
        session.setAttribute("acctId",loginSysUserTokenVo.getLoginSysUserVo().getId());//用用户id当做acctid
        return ApiResult.ok(loginSysUserTokenVo, "登陆成功");
    }

    @PostMapping("/logout")
    public ApiResult logout(HttpServletRequest request) throws Exception {
        loginService.logout(request);
        return ApiResult.ok("退出成功");
    }
}
