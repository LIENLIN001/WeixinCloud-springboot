package com.tencent.wxcloudrun.dto;

import lombok.Data;

@Data
public class DraftArticleRequest {
    private String article_type = "news"; // 非必填，默认news
    private String title; // 必填
    private String author; // 非必填
    private String digest; // 非必填
    private String content; // 必填
    private String content_source_url; // 非必填
    private String thumb_media_id; // 必填
    private Integer need_open_comment = 0; // 非必填，默认0
    private Integer only_fans_can_comment = 0; // 非必填，默认0
}