package io.geekidea.springbootplus.test.controller;

import io.geekidea.springbootplus.common.api.ApiCode;
import io.geekidea.springbootplus.test.concurrent.RandomAccountLocker;
import io.geekidea.springbootplus.test.entity.AccountInfo;
import io.geekidea.springbootplus.test.service.AccountInfoService;
import io.geekidea.springbootplus.test.param.AccountInfoQueryParam;
import io.geekidea.springbootplus.test.vo.AccountInfoQueryVo;
import io.geekidea.springbootplus.common.api.ApiResult;
import io.geekidea.springbootplus.common.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import io.geekidea.springbootplus.common.vo.Paging;
import io.geekidea.springbootplus.common.param.IdParam;

import java.math.BigDecimal;

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
    @Autowired
    private RandomAccountLocker randomAccountLocker;

    //@RequiresRoles("admin")
    //测试未创建权限相关数据
    @RequestMapping(value = "/detail", method = {RequestMethod.GET, RequestMethod.POST},produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "查询指定账户余额", notes = "查询账户余额")
    public ApiResult<AccountInfo> queryAcctDetail(@ApiParam(value = "账户id", required = true)
                                         @RequestParam(value="id",required = false) Long acctId){
        //默认只能查询当前用户的账户余额，如果需要查询其他账号余额需要进行权限判定
        HttpSession session = getRequest().getSession();
        if(session == null){
            return ApiResult.fail(ApiCode.UNAUTHORIZED,"请登录以后再查询");
        }
        Long sessionAcctId = (Long)session.getAttribute("acctId");
        String role = (String)session.getAttribute("role");
        //此处需进行权限判定
        if("admin".equals(role)){
            //
        }else{

        }
        if(acctId != null) {
            AccountInfo accountInfo = accountInfoService.getById(acctId);
            if (accountInfo == null) {
                return ApiResult.fail("该账号不存在");
            }
            accountInfo.setCreateTime(null);//隐藏部分信息
            accountInfo.setUpdateTime(null);//隐藏部分信息
            return ApiResult.ok(accountInfo);
        }else{
            return ApiResult.fail("查询条件错误");
        }
    }


    @RequestMapping(value = "/transfer", method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "内部转账", notes = "内部转账交易，交易成功则返回当前账号的信息")
    public ApiResult<AccountInfo> acctTransfer(@ApiParam(value = "交易要素", required = true)
                                      @RequestBody TransactionRequest transactionRequest){
        if(transactionRequest == null){
            return ApiResult.fail("请求错误");
        }
        int type = transactionRequest.getType();
        if(type != TransactionRequest.DEBE_转账){
            return ApiResult.fail("交易类型错误");
        }
        String amount = transactionRequest.getAmount();
        if(amount == null || amount.trim().length() == 0){
            return ApiResult.fail("交易金额不允许为空");
        }
        ApiResult apiResult = checkAcctInfo(transactionRequest);
        if(apiResult != null){
            return apiResult;
        }else{
            apiResult = doTransfer(transactionRequest);
            if(apiResult != null){
                return apiResult;
            }else{
                return ApiResult.fail();
            }
        }
    }

    private ApiResult<AccountInfo> checkAcctInfo(TransactionRequest transactionRequest){
        Long acctId = transactionRequest.getAcctId();
        Long targetAcctId = transactionRequest.getTargetAcctId();
        AccountInfo accountInfo,targetAccountInfo;
        if(acctId != null && acctId.longValue() > 0){
            accountInfo = accountInfoService.getById(acctId);
            if(accountInfo == null){
                return  ApiResult.fail("当前账号不存在");
            }else{
                if(!transactionRequest.getAcctNo().equals(accountInfo.getAcctNo()) ||
                        !transactionRequest.getAcctName().equals(accountInfo.getAcctName())){
                    return  ApiResult.fail("当前账号信息错误");
                }
            }
        }else{
            return ApiResult.fail("必须填写账号id");
        }
        if(targetAcctId != null && acctId.longValue() > 0){
            targetAccountInfo = accountInfoService.getById(targetAcctId);
            if(targetAccountInfo == null){
                return ApiResult.fail("对方账号不存在");
            }else{
                if(!transactionRequest.getTargetAcctNo().equals(targetAccountInfo.getAcctNo()) ||
                        !transactionRequest.getTargetAcctName().equals(targetAccountInfo.getAcctName())){
                    return  ApiResult.fail("对方账号信息不一致");
                }
            }
        }else{
            return ApiResult.fail("必须填写对方账号id");
        }
        //检查金额格式
        String amount = transactionRequest.getAmount();
        try{
            BigDecimal number = new BigDecimal(amount);
            if(number.doubleValue() <=0){
                log.error("交易金额必须为大于0,{}",transactionRequest);
                return ApiResult.fail("金额必须为正");
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
            log.error("交易金额格式不对,{}",transactionRequest);
            return ApiResult.fail("交易金额格式不对");
        }

        return null;

    }

    private ApiResult<AccountInfo> doTransfer(TransactionRequest transactionRequest){
        Long acctId,targetAcctId;
        String acctNo,targetAcctNo;
        AccountInfo accountInfo,targetAccountInfo;
        acctId = transactionRequest.getAcctId();
        acctNo = transactionRequest.getAcctNo();
        targetAcctId = transactionRequest.getTargetAcctId();
        targetAcctNo = transactionRequest.getTargetAcctNo();
        ApiResult<AccountInfo> result = null;
        int timeoutTime = 5000,maxLockTime = 5000;//5秒无法获取到锁则超时，转账失败，如果取到锁，最多锁定5秒
        String acctLocker = null,targetAcctLocker = null;
        try {
            acctLocker = randomAccountLocker.getWriteLock(acctId,acctNo,timeoutTime,maxLockTime);
            if(acctLocker == null){
                log.error("转账交易失败，无法获取到账户锁,acctid={},acctno={}",acctId,acctNo);
                return ApiResult.fail("系统繁忙，请稍后再试");
            }
            targetAcctLocker = randomAccountLocker.getWriteLock(targetAcctId,targetAcctNo,timeoutTime,maxLockTime);
            if(targetAcctLocker == null){
                log.error("转账交易失败，无法获取到对方账户锁,targetAcctId={},targetAcctNo={}",targetAcctId,targetAcctNo);
                return ApiResult.fail("系统繁忙，请稍后再试");
            }
            //再次查询账户信息
            accountInfo = accountInfoService.getById(acctId);
            targetAccountInfo = accountInfoService.getById(targetAcctId);
            BigDecimal transactionAmount = new BigDecimal(transactionRequest.getAmount());
            //检查账户余额
            if(accountInfo.getBalance().subtract(transactionAmount).doubleValue() < 0){
                log.error("当前账号:acctId:{},acctNo:{} 可用余额 {}不足:,交易明细：{}",acctId,acctNo,accountInfo.getBalance().toString(),transactionRequest);
                return ApiResult.fail("当前账号余额不足");
            }
            accountInfo.setBalance(accountInfo.getBalance().subtract(transactionAmount));
            targetAccountInfo.setBalance(targetAccountInfo.getBalance().add(transactionAmount).setScale(2,BigDecimal.ROUND_CEILING));
            accountInfoService.updateAccount(accountInfo,targetAccountInfo);
            randomAccountLocker.releaseWriteLock(acctId,acctNo,acctLocker);
            randomAccountLocker.releaseWriteLock(targetAcctId,targetAcctNo,targetAcctLocker);
            acctLocker = null;
            targetAcctLocker = null;
            accountInfo.setCreateTime(null);
            accountInfo.setUpdateTime(null);
            result = ApiResult.ok(accountInfo);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        } finally {
            if(acctLocker != null){
                randomAccountLocker.releaseWriteLock(acctId,acctNo,acctLocker);
            }
            if(targetAcctLocker != null){
                randomAccountLocker.releaseWriteLock(targetAcctId,targetAcctNo,targetAcctLocker);
            }
        }
        return result;
    }



}

