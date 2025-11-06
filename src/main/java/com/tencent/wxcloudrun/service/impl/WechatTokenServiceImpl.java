package com.tencent.wxcloudrun.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.wxcloudrun.dto.WechatTokenRequest;
import com.tencent.wxcloudrun.dto.WechatTokenResponse;
import com.tencent.wxcloudrun.service.WechatTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WechatTokenServiceImpl implements WechatTokenService {

    private static final Logger logger = LoggerFactory.getLogger(WechatTokenServiceImpl.class);
    private static final String WECHAT_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/stable_token";

    @Override
    public WechatTokenResponse getStableToken(WechatTokenRequest request) {
        logger.info("开始获取微信stable_token, appid: {}", request.getAppid());
        
        RestTemplate restTemplate = new RestTemplate();
        
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        // 构造请求体
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("appid", request.getAppid());
        requestBody.put("secret", request.getSecret());
        requestBody.put("grant_type", request.getGrant_type());
        
        // 创建HttpEntity
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
        
        try {
            // 发送POST请求
            ResponseEntity<String> response = restTemplate.postForEntity(WECHAT_TOKEN_URL, entity, String.class);
            
            logger.info("微信stable_token接口响应状态: {}", response.getStatusCode());
            logger.debug("微信stable_token接口响应内容: {}", response.getBody());
            
            // 检查HTTP状态码
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("微信接口调用失败，HTTP状态码: " + response.getStatusCode());
            }
            
            // 解析响应
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            
            // 检查微信返回的错误码
            if (jsonNode.has("errcode") && jsonNode.get("errcode").asInt() != 0) {
                int errcode = jsonNode.get("errcode").asInt();
                String errmsg = jsonNode.has("errmsg") ? jsonNode.get("errmsg").asText() : "未知错误";
                throw new RuntimeException("微信接口返回错误，错误码: " + errcode + "，错误信息: " + errmsg);
            }
            
            // 转换为WechatTokenResponse对象
            WechatTokenResponse tokenResponse = new WechatTokenResponse();
            tokenResponse.setAccess_token(jsonNode.get("access_token").asText());
            tokenResponse.setExpires_in(jsonNode.get("expires_in").asInt());
            
            logger.info("成功获取微信stable_token，有效期: {}秒", tokenResponse.getExpires_in());
            return tokenResponse;
        } catch (Exception e) {
            logger.error("获取微信stable_token失败", e);
            throw new RuntimeException("获取微信stable_token失败: " + e.getMessage(), e);
        }
    }
}