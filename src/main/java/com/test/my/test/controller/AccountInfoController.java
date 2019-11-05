package com.test.my.test.controller;

import com.test.my.test.entity.AccountInfo;
import com.test.my.test.service.AccountInfoService;
import io.geekidea.springbootplus.common.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <pre>
 *  前端控制器
 * </pre>
 *
 * @author test
 * @since 2019-11-05
 */
@Slf4j
@RestController
@RequestMapping("/accountInfo")
@Api(" API")
public class AccountInfoController extends BaseController {

    @Autowired
    private AccountInfoService accountInfoService;


}

