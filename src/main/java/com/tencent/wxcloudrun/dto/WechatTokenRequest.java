package com.tencent.wxcloudrun.dto;

import lombok.Data;

@Data
public class WechatTokenRequest {
    private String appid;
    private String secret;
    private String grant_type;
}