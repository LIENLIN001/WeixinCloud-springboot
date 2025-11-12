package com.tencent.wxcloudrun.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.wxcloudrun.dto.CreateDraftRequest;
import com.tencent.wxcloudrun.dto.CreateDraftResponse;
import com.tencent.wxcloudrun.dto.PublishArticleRequest;
import com.tencent.wxcloudrun.dto.PublishArticleResponse;
import com.tencent.wxcloudrun.dto.UploadMaterialResponse;
import com.tencent.wxcloudrun.dto.WechatTokenRequest;
import com.tencent.wxcloudrun.dto.WechatTokenResponse;
import com.tencent.wxcloudrun.service.WechatTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
public class WechatTokenServiceImpl implements WechatTokenService {

    private static final Logger logger = LoggerFactory.getLogger(WechatTokenServiceImpl.class);
    private static final String WECHAT_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/stable_token";
    private static final String WECHAT_DRAFT_ADD_URL = "https://api.weixin.qq.com/cgi-bin/draft/add";
    private static final String WECHAT_ARTICLE_PUBLISH_URL = "https://api.weixin.qq.com/cgi-bin/freepublish/submit";
    private static final String WECHAT_MATERIAL_ADD_URL = "https://api.weixin.qq.com/cgi-bin/material/add_material";

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

    @Override
    public CreateDraftResponse createDraft(String accessToken, CreateDraftRequest request) {
        logger.info("开始创建微信公众号草稿");
        
        RestTemplate restTemplate = new RestTemplate();
        
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        // 构造URL
        String url = WECHAT_DRAFT_ADD_URL + "?access_token=" + accessToken;
        
        // 创建HttpEntity
        HttpEntity<CreateDraftRequest> entity = new HttpEntity<>(request, headers);
        
        try {
            // 发送POST请求
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            
            logger.info("微信创建草稿接口响应状态: {}", response.getStatusCode());
            logger.debug("微信创建草稿接口响应内容: {}", response.getBody());
            
            // 检查HTTP状态码
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("微信接口调用失败，HTTP状态码: " + response.getStatusCode());
            }
            
            // 解析响应
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            
            // 转换为CreateDraftResponse对象
            CreateDraftResponse draftResponse = new CreateDraftResponse();
            
            // 检查是否有错误码
            if (jsonNode.has("errcode")) {
                draftResponse.setErrcode(jsonNode.get("errcode").asInt());
            }
            
            if (jsonNode.has("errmsg")) {
                draftResponse.setErrmsg(jsonNode.get("errmsg").asText());
            }
            
            // 如果创建成功，设置media_id
            if (jsonNode.has("media_id")) {
                draftResponse.setMedia_id(jsonNode.get("media_id").asText());
            }
            
            logger.info("创建微信公众号草稿完成");
            return draftResponse;
        } catch (Exception e) {
            logger.error("创建微信公众号草稿失败", e);
            throw new RuntimeException("创建微信公众号草稿失败: " + e.getMessage(), e);
        }
    }

    @Override
    public PublishArticleResponse publishArticle(String accessToken, PublishArticleRequest request) {
        logger.info("开始发布微信公众号文章");
        
        RestTemplate restTemplate = new RestTemplate();
        
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        // 构造URL
        String url = WECHAT_ARTICLE_PUBLISH_URL + "?access_token=" + accessToken;
        
        // 创建HttpEntity
        HttpEntity<PublishArticleRequest> entity = new HttpEntity<>(request, headers);
        
        try {
            // 发送POST请求
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            
            logger.info("微信发布文章接口响应状态: {}", response.getStatusCode());
            logger.debug("微信发布文章接口响应内容: {}", response.getBody());
            
            // 检查HTTP状态码
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("微信接口调用失败，HTTP状态码: " + response.getStatusCode());
            }
            
            // 解析响应
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            
            // 转换为PublishArticleResponse对象
            PublishArticleResponse publishResponse = new PublishArticleResponse();
            
            // 检查是否有错误码
            if (jsonNode.has("errcode")) {
                publishResponse.setErrcode(jsonNode.get("errcode").asInt());
            }
            
            if (jsonNode.has("errmsg")) {
                publishResponse.setErrmsg(jsonNode.get("errmsg").asText());
            }
            
            // 如果发布成功，设置publish_id
            if (jsonNode.has("publish_id")) {
                publishResponse.setPublish_id(jsonNode.get("publish_id").asText());
            }
            
            logger.info("发布微信公众号文章完成");
            return publishResponse;
        } catch (Exception e) {
            logger.error("发布微信公众号文章失败", e);
            throw new RuntimeException("发布微信公众号文章失败: " + e.getMessage(), e);
        }
    }

    @Override
    public UploadMaterialResponse uploadMaterial(String accessToken, String type, MultipartFile media, String title, String introduction) {
        logger.info("开始上传微信永久素材，类型: {}", type);
        
        try {
            RestTemplate restTemplate = new RestTemplate();
            
            // 构造URL
            String url = WECHAT_MATERIAL_ADD_URL + "?access_token=" + accessToken + "&type=" + type;
            
            // 创建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            
            // 创建请求体
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            
            // 添加文件
            body.add("media", media.getResource());
            
            // 如果是视频类型，添加描述信息
            if ("video".equals(type)) {
                Map<String, String> description = new HashMap<>();
                description.put("title", title);
                description.put("introduction", introduction);
                body.add("description", new ObjectMapper().writeValueAsString(description));
            }
            
            // 创建HttpEntity
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            
            // 发送POST请求
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
            
            logger.info("微信上传素材接口响应状态: {}", response.getStatusCode());
            logger.debug("微信上传素材接口响应内容: {}", response.getBody());
            
            // 检查HTTP状态码
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("微信接口调用失败，HTTP状态码: " + response.getStatusCode());
            }
            
            // 解析响应
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            
            // 转换为UploadMaterialResponse对象
            UploadMaterialResponse materialResponse = new UploadMaterialResponse();
            
            // 检查是否有错误码
            if (jsonNode.has("errcode")) {
                materialResponse.setErrcode(jsonNode.get("errcode").asInt());
                if (jsonNode.has("errmsg")) {
                    materialResponse.setErrmsg(jsonNode.get("errmsg").asText());
                }
            }
            
            // 如果上传成功，设置media_id和url
            if (jsonNode.has("media_id")) {
                materialResponse.setMedia_id(jsonNode.get("media_id").asText());
            }
            
            if (jsonNode.has("url")) {
                materialResponse.setUrl(jsonNode.get("url").asText());
            }
            
            logger.info("上传微信永久素材完成");
            return materialResponse;
        } catch (Exception e) {
            logger.error("上传微信永久素材失败", e);
            throw new RuntimeException("上传微信永久素材失败: " + e.getMessage(), e);
        }
    }
}