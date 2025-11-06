package com.tencent.wxcloudrun.dto;

import lombok.Data;

@Data
public class WechatTokenResponse {
    private String access_token;
    private Integer expires_in;
}