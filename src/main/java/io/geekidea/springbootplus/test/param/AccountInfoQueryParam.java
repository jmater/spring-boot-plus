package io.geekidea.springbootplus.test.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import io.geekidea.springbootplus.common.param.OrderQueryParam;

/**
 * <pre>
 *  查询参数对象
 * </pre>
 *
 * @author test
 * @date 2019-11-05
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "AccountInfoQueryParam对象", description = "查询参数")
public class AccountInfoQueryParam extends OrderQueryParam {
    private static final long serialVersionUID = 1L;
}
