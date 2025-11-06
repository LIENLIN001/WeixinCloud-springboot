package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.WechatTokenRequest;
import com.tencent.wxcloudrun.dto.WechatTokenResponse;
import com.tencent.wxcloudrun.service.WechatTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WechatTokenController {

    private final WechatTokenService wechatTokenService;
    private final Logger logger;

    public WechatTokenController(@Autowired WechatTokenService wechatTokenService) {
        this.wechatTokenService = wechatTokenService;
        this.logger = LoggerFactory.getLogger(WechatTokenController.class);
    }

    /**
     * 获取微信stable_token
     * @param request 包含appid、secret和grant_type的请求对象
     * @return API response json
     */
    @PostMapping(value = "/api/wechat/token")
    ApiResponse getWechatStableToken(@RequestBody WechatTokenRequest request) {
        logger.info("/api/wechat/token post request, appid: {}", request.getAppid());
        
        // 参数校验
        if (request.getAppid() == null || request.getAppid().isEmpty()) {
            logger.warn("appid不能为空");
            return ApiResponse.error("appid不能为空");
        }
        
        if (request.getSecret() == null || request.getSecret().isEmpty()) {
            logger.warn("secret不能为空");
            return ApiResponse.error("secret不能为空");
        }
        
        if (request.getGrant_type() == null || request.getGrant_type().isEmpty()) {
            logger.warn("grant_type不能为空");
            return ApiResponse.error("grant_type不能为空");
        }
        
        try {
            WechatTokenResponse response = wechatTokenService.getStableToken(request);
            logger.info("成功获取微信stable_token");
            return ApiResponse.ok(response);
        } catch (Exception e) {
            logger.error("获取微信stable_token失败", e);
            return ApiResponse.error("获取微信stable_token失败: " + e.getMessage());
        }
    }
}