package com.tencent.wxcloudrun.dto;

import lombok.Data;

@Data
public class CreateDraftResponse {
    private String media_id;
    private Integer errcode;
    private String errmsg;
}