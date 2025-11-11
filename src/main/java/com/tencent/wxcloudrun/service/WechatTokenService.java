package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.CreateDraftRequest;
import com.tencent.wxcloudrun.dto.CreateDraftResponse;
import com.tencent.wxcloudrun.dto.WechatTokenRequest;
import com.tencent.wxcloudrun.dto.WechatTokenResponse;

public interface WechatTokenService {

    /**
     * 获取微信stable_token
     * @param request 包含appid、secret和grant_type的请求对象
     * @return 微信token响应对象
     */
    WechatTokenResponse getStableToken(WechatTokenRequest request);

    /**
     * 创建微信公众号草稿
     * @param accessToken 微信访问令牌
     * @param request 草稿内容请求对象
     * @return 创建草稿响应对象
     */
    CreateDraftResponse createDraft(String accessToken, CreateDraftRequest request);
}