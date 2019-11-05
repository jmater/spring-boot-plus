package com.test.my.test.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.my.test.entity.AccountInfo;
import io.geekidea.springbootplus.test.param.AccountInfoQueryParam;
import io.geekidea.springbootplus.test.vo.AccountInfoQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <pre>
 *  Mapper 接口
 * </pre>
 *
 * @author test
 * @since 2019-11-05
 */
@Repository
public interface AccountInfoMapper extends BaseMapper<AccountInfo> {

    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     */
    AccountInfoQueryVo getAccountInfoById(Serializable id);

    /**
     * 获取分页对象
     *
     * @param page
     * @param accountInfoQueryParam
     * @return
     */
    IPage<AccountInfoQueryVo> getAccountInfoPageList(@Param("page") Page page, @Param("param") AccountInfoQueryParam accountInfoQueryParam);

}
