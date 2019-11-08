package io.geekidea.springbootplus.test;

import cn.hutool.json.JSONUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import io.geekidea.springbootplus.SpringBootPlusApplication;
import io.geekidea.springbootplus.common.api.ApiResult;
import io.geekidea.springbootplus.test.controller.AccountInfoController;
import io.geekidea.springbootplus.test.controller.TransactionRequest;
import io.geekidea.springbootplus.test.entity.AccountInfo;
import io.geekidea.springbootplus.test.service.AccountInfoService;
import io.geekidea.springbootplus.test.service.impl.AccountInfoServiceImpl;
import io.geekidea.springbootplus.util.Jackson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestAcct {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    AccountInfoServiceImpl accountInfoService;

    @Autowired
    private WebApplicationContext webContext;

    @Before
    public void setupMockMvc() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
        accountInfoService.getTempAcctInfo();
    }

    @Test
    public void test() throws Exception {
        testSuccessTransfer();
        testSuccessTransferFailed();
        testTransferTwice();
        testQueryAcct(1L);
        testQueryAcct(2L);
    }

    public void testSuccessTransfer() throws Exception{
        AccountInfo acct1 = new AccountInfo();
        acct1.setId(1L);
        acct1.setAcctNo("10001");
        acct1.setAcctName("张三");

        AccountInfo acct2 = new AccountInfo();
        acct2.setId(2L);
        acct2.setAcctNo("200001");
        acct2.setAcctName("李四");

        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAcctId(acct1.getId());
        transactionRequest.setAcctNo(acct1.getAcctNo());
        transactionRequest.setAcctName(acct1.getAcctName());

        transactionRequest.setTargetAcctId(acct2.getId());
        transactionRequest.setTargetAcctNo(acct2.getAcctNo());
        transactionRequest.setTargetAcctName(acct2.getAcctName());

        transactionRequest.setAmount("800.00");
        transactionRequest.setRemark("测试");
        transactionRequest.setType(TransactionRequest.DEBE_转账);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/accountInfo/transfer").contentType("application/json")
                .content(Jackson.toJsonString(transactionRequest));
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String json = result.getResponse().getContentAsString(Charset.forName("UTF-8"));
        ApiResult<AccountInfo> apiResult = JSON.parseObject(json, new TypeReference<ApiResult<AccountInfo>>(){});
        Assert.assertEquals(apiResult.getData().getBalance().doubleValue(),4200.00,0.001);
        System.out.println(json);
    }

    public void testSuccessTransferFailed() throws Exception{
        AccountInfo acct1 = new AccountInfo();
        acct1.setId(1L);
        acct1.setAcctNo("10001");
        acct1.setAcctName("张三");

        AccountInfo acct2 = new AccountInfo();
        acct2.setId(2L);
        acct2.setAcctNo("200001");
        acct2.setAcctName("李四");

        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAcctId(acct1.getId());
        transactionRequest.setAcctNo(acct1.getAcctNo());
        transactionRequest.setAcctName(acct1.getAcctName());

        transactionRequest.setTargetAcctId(acct2.getId());
        transactionRequest.setTargetAcctNo(acct2.getAcctNo());
        transactionRequest.setTargetAcctName(acct2.getAcctName());

        transactionRequest.setAmount("80000000.00");
        transactionRequest.setRemark("测试");
        transactionRequest.setType(TransactionRequest.DEBE_转账);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/accountInfo/transfer").contentType("application/json")
                .content(Jackson.toJsonString(transactionRequest));
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String json = result.getResponse().getContentAsString(Charset.forName("UTF-8"));
        ApiResult<AccountInfo> apiResult = JSON.parseObject(json, new TypeReference<ApiResult<AccountInfo>>(){});
        Assert.assertFalse(apiResult.isSuccess());
        System.out.println(json);

        transactionRequest.setAmount("0.00");
        transactionRequest.setRemark("测试");
        transactionRequest.setType(TransactionRequest.DEBE_转账);

        requestBuilder = MockMvcRequestBuilders.post("/accountInfo/transfer").contentType("application/json")
                .content(Jackson.toJsonString(transactionRequest));
        result = mockMvc.perform(requestBuilder).andReturn();
        json = result.getResponse().getContentAsString(Charset.forName("UTF-8"));
        apiResult = JSON.parseObject(json, new TypeReference<ApiResult<AccountInfo>>(){});
        Assert.assertFalse(apiResult.isSuccess());
        System.out.println(json);

        transactionRequest.setAmount("-100.00");
        transactionRequest.setRemark("测试");
        transactionRequest.setType(TransactionRequest.DEBE_转账);

        requestBuilder = MockMvcRequestBuilders.post("/accountInfo/transfer").contentType("application/json")
                .content(Jackson.toJsonString(transactionRequest));
        result = mockMvc.perform(requestBuilder).andReturn();
        json = result.getResponse().getContentAsString(Charset.forName("UTF-8"));
        apiResult = JSON.parseObject(json, new TypeReference<ApiResult<AccountInfo>>(){});
        Assert.assertFalse(apiResult.isSuccess());
        System.out.println(json);

    }

    public void testTransferTwice() throws Exception{
        AccountInfo acct1 = new AccountInfo();
        acct1.setId(1L);
        acct1.setAcctNo("10001");
        acct1.setAcctName("张三");

        AccountInfo acct2 = new AccountInfo();
        acct2.setId(2L);
        acct2.setAcctNo("200001");
        acct2.setAcctName("李四");

        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAcctId(acct1.getId());
        transactionRequest.setAcctNo(acct1.getAcctNo());
        transactionRequest.setAcctName(acct1.getAcctName());

        transactionRequest.setTargetAcctId(acct2.getId());
        transactionRequest.setTargetAcctNo(acct2.getAcctNo());
        transactionRequest.setTargetAcctName(acct2.getAcctName());

        transactionRequest.setAmount("10.00");
        transactionRequest.setRemark("测试");
        transactionRequest.setType(TransactionRequest.DEBE_转账);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/accountInfo/transfer").contentType("application/json")
                .content(Jackson.toJsonString(transactionRequest));
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String json = result.getResponse().getContentAsString(Charset.forName("UTF-8"));
        ApiResult<AccountInfo> apiResult = JSON.parseObject(json, new TypeReference<ApiResult<AccountInfo>>(){});
        Assert.assertEquals(4190.00,apiResult.getData().getBalance().doubleValue(),0.001);

        transactionRequest.setAmount("10.00");
        transactionRequest.setRemark("测试");
        transactionRequest.setType(TransactionRequest.DEBE_转账);

        requestBuilder = MockMvcRequestBuilders.post("/accountInfo/transfer").contentType("application/json")
                .content(Jackson.toJsonString(transactionRequest));
        result = mockMvc.perform(requestBuilder).andReturn();
        json = result.getResponse().getContentAsString(Charset.forName("UTF-8"));
        apiResult = JSON.parseObject(json, new TypeReference<ApiResult<AccountInfo>>(){});

        Assert.assertEquals(4180.00,apiResult.getData().getBalance().doubleValue(),0.001);
        System.out.println(json);
    }

    private void testQueryAcct(Long id) throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/accountInfo/detail").param("id",String.valueOf(id));
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String json = result.getResponse().getContentAsString(Charset.forName("UTF-8"));
        ApiResult<AccountInfo> apiResult = JSON.parseObject(json, new TypeReference<ApiResult<AccountInfo>>(){});
        if(id == 1L){
            Assert.assertEquals(4180.00,apiResult.getData().getBalance().doubleValue(),0.001);
        }
        if(id == 2L){
            Assert.assertEquals(920.00,apiResult.getData().getBalance().doubleValue(),0.001);
        }
        System.out.println(json);
    }
}
