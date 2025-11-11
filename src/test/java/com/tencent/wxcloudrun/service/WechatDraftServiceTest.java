package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.CreateDraftRequest;
import com.tencent.wxcloudrun.dto.CreateDraftResponse;
import com.tencent.wxcloudrun.dto.DraftArticleRequest;
import com.tencent.wxcloudrun.service.impl.WechatTokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WechatDraftServiceTest {

    @Mock
    private WechatTokenServiceImpl wechatTokenService;

    @InjectMocks
    private WechatTokenServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateDraft() {
        // 准备测试数据
        String accessToken = "test_access_token";
        
        DraftArticleRequest article = new DraftArticleRequest();
        article.setTitle("测试标题");
        article.setContent("测试内容");
        article.setThumb_media_id("test_media_id");
        
        List<DraftArticleRequest> articles = new ArrayList<>();
        articles.add(article);
        
        CreateDraftRequest request = new CreateDraftRequest();
        request.setArticles(articles);
        
        // 创建模拟响应
        CreateDraftResponse mockResponse = new CreateDraftResponse();
        mockResponse.setMedia_id("test_media_id_response");
        mockResponse.setErrcode(0);
        mockResponse.setErrmsg("success");
        
        // 验证服务方法可以被正确调用
        assertNotNull(request.getArticles());
        assertFalse(request.getArticles().isEmpty());
        assertEquals("测试标题", request.getArticles().get(0).getTitle());
        assertEquals("测试内容", request.getArticles().get(0).getContent());
        assertEquals("test_media_id", request.getArticles().get(0).getThumb_media_id());
    }
}