package io.geekidea.springbootplus.test.service;

import io.geekidea.springbootplus.test.entity.AccountInfo;
import io.geekidea.springbootplus.common.service.BaseService;
import io.geekidea.springbootplus.test.param.AccountInfoQueryParam;
import io.geekidea.springbootplus.test.vo.AccountInfoQueryVo;
import io.geekidea.springbootplus.common.vo.Paging;

import java.io.Serializable;

/**
 * <pre>
 *  服务类
 * </pre>
 *
 * @author test
 * @since 2019-11-05
 */
public interface AccountInfoService extends BaseService<AccountInfo> {

    boolean updateAccount(AccountInfo accountInfo,AccountInfo targetAccountInfo);

}
