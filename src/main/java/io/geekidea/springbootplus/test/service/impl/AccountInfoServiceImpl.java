package io.geekidea.springbootplus.test.service.impl;

import io.geekidea.springbootplus.test.entity.AccountInfo;
import io.geekidea.springbootplus.test.mapper.AccountInfoMapper;
import io.geekidea.springbootplus.test.service.AccountInfoService;
import io.geekidea.springbootplus.test.param.AccountInfoQueryParam;
import io.geekidea.springbootplus.test.vo.AccountInfoQueryVo;
import io.geekidea.springbootplus.common.service.impl.BaseServiceImpl;
import io.geekidea.springbootplus.common.vo.Paging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


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
    private Map<Long,AccountInfo> tempAcctInfo;

    public Map<Long, AccountInfo> getTempAcctInfo() {
        return tempAcctInfo;
    }

    public void setTempAcctInfo(Map<Long, AccountInfo> tempAcctInfo) {
        this.tempAcctInfo = tempAcctInfo;
    }

    @PostConstruct
    public void init(){
        tempAcctInfo = new ConcurrentHashMap<>();
        AccountInfo a1 = new AccountInfo();
        a1.setId(1L);
        a1.setAcctNo("10001");
        a1.setAcctName("张三");
        a1.setBalance(new BigDecimal("5000.00"));
        a1.setCreateTime(new Date());
        a1.setUpdateTime(new Date());

        AccountInfo a2 = new AccountInfo();
        a2.setId(2L);
        a2.setAcctNo("200001");
        a2.setAcctName("李四");
        a2.setBalance(new BigDecimal("100.00"));
        a2.setCreateTime(new Date());
        a2.setUpdateTime(new Date());

        tempAcctInfo.put(1L,a1);
        tempAcctInfo.put(2L,a2);

    }


    @Override
    public AccountInfo getById(Serializable id) {
        Long acctId = (Long)id;
        //return accountInfoMapper.selectById(acctId);
        return tempAcctInfo.get(acctId);
    }

    @Transactional
    @Override
    public boolean updateAccount(AccountInfo accountInfo,AccountInfo targetAccountInfo){
        //accountInfoMapper.updateById(accountInfo);
        //accountInfoMapper.updateById(targetAccountInfo);
        accountInfo.setUpdateTime(new Date());
        targetAccountInfo.setUpdateTime(new Date());
        tempAcctInfo.put(accountInfo.getId(),accountInfo);
        tempAcctInfo.put(targetAccountInfo.getId(),targetAccountInfo);
        return true;
    }

}
