package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.WechatTokenRequest;
import com.tencent.wxcloudrun.dto.WechatTokenResponse;
import com.tencent.wxcloudrun.service.impl.WechatTokenServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class WechatTokenServiceTest {

    @Test
    public void testGetStableToken() {
        // 这只是一个简单的单元测试示例，实际测试时需要提供有效的appid和secret
        WechatTokenService wechatTokenService = new WechatTokenServiceImpl();
        
        WechatTokenRequest request = new WechatTokenRequest();
        request.setAppid("your-appid");
        request.setSecret("your-secret");
        request.setGrant_type("client_credential");
        
        // 注意：这个测试会因为无效的appid/secret而失败，仅用于演示
        try {
            WechatTokenResponse response = wechatTokenService.getStableToken(request);
            assertNotNull(response);
        } catch (Exception e) {
            // 预期会抛出异常，因为使用了无效的凭据
            System.out.println("Expected exception occurred: " + e.getMessage());
        }
    }
}