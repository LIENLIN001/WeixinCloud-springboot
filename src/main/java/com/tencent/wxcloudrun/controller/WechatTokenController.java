package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.CreateDraftRequest;
import com.tencent.wxcloudrun.dto.CreateDraftResponse;
import com.tencent.wxcloudrun.dto.DraftArticleRequest;
import com.tencent.wxcloudrun.dto.WechatTokenRequest;
import com.tencent.wxcloudrun.dto.WechatTokenResponse;
import com.tencent.wxcloudrun.service.WechatTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 创建微信公众号草稿
     * @param accessToken 微信访问令牌
     * @param request 草稿内容请求对象
     * @return API response json
     */
    @PostMapping(value = "/api/wechat/draft")
    ApiResponse createWechatDraft(@RequestParam String accessToken, @RequestBody CreateDraftRequest request) {
        logger.info("/api/wechat/draft post request");
        
        // 参数校验
        if (accessToken == null || accessToken.isEmpty()) {
            logger.warn("access_token不能为空");
            return ApiResponse.error("access_token不能为空");
        }
        
        if (request.getArticles() == null || request.getArticles().isEmpty()) {
            logger.warn("articles不能为空");
            return ApiResponse.error("articles不能为空");
        }
        
        // 校验每个文章的必填字段
        for (int i = 0; i < request.getArticles().size(); i++) {
            DraftArticleRequest article = request.getArticles().get(i);
            if (article.getTitle() == null || article.getTitle().isEmpty()) {
                logger.warn("第{}篇文章的title不能为空", i + 1);
                return ApiResponse.error("第" + (i + 1) + "篇文章的title不能为空");
            }
            
            if (article.getContent() == null || article.getContent().isEmpty()) {
                logger.warn("第{}篇文章的content不能为空", i + 1);
                return ApiResponse.error("第" + (i + 1) + "篇文章的content不能为空");
            }
            
            if (article.getThumb_media_id() == null || article.getThumb_media_id().isEmpty()) {
                logger.warn("第{}篇文章的thumb_media_id不能为空", i + 1);
                return ApiResponse.error("第" + (i + 1) + "篇文章的thumb_media_id不能为空");
            }
        }
        
        try {
            CreateDraftResponse response = wechatTokenService.createDraft(accessToken, request);
            logger.info("成功创建微信公众号草稿");
            
            // 检查微信返回的错误码
            if (response.getErrcode() != null && response.getErrcode() != 0) {
                return ApiResponse.error("微信接口返回错误，错误码: " + response.getErrcode() + "，错误信息: " + response.getErrmsg());
            }
            
            return ApiResponse.ok(response);
        } catch (Exception e) {
            logger.error("创建微信公众号草稿失败", e);
            return ApiResponse.error("创建微信公众号草稿失败: " + e.getMessage());
        }
    }
}