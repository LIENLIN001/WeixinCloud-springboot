package com.tencent.wxcloudrun.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadMaterialRequest {
    private MultipartFile media; // 必填，文件类型
    private Description description;

    @Data
    public static class Description {
        private String title; // VIDEO_TITLE
        private String introduction; // INTRODUCTION
    }
}