package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.WechatTokenRequest;
import com.tencent.wxcloudrun.dto.WechatTokenResponse;

public interface WechatTokenService {

    /**
     * 获取微信stable_token
     * @param request 包含appid、secret和grant_type的请求对象
     * @return 微信token响应对象
     */
    WechatTokenResponse getStableToken(WechatTokenRequest request);
}