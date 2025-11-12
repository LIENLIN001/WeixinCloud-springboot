package com.tencent.wxcloudrun.dto;

import lombok.Data;

@Data
public class PublishArticleResponse {
    private Integer errcode;
    private String errmsg;
    private String publish_id;
}