package com.test.my.test.service.impl;

import com.test.my.test.entity.AccountInfo;
import com.test.my.test.mapper.AccountInfoMapper;
import com.test.my.test.service.AccountInfoService;
import io.geekidea.springbootplus.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * <pre>
 *  服务实现类
 * </pre>
 *
 * @author test
 * @since 2019-11-05
 */
@Slf4j
@Service
public class AccountInfoServiceImpl extends BaseServiceImpl<AccountInfoMapper, AccountInfo> implements AccountInfoService {

    @Autowired
    private AccountInfoMapper accountInfoMapper;

}
