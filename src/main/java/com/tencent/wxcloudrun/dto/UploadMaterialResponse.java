package com.tencent.wxcloudrun.dto;

import lombok.Data;

@Data
public class UploadMaterialResponse {
    private String media_id;
    private String url;
    private Integer errcode;
    private String errmsg;
}