package com.tencent.wxcloudrun.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateDraftRequest {
    private List<DraftArticleRequest> articles;
}