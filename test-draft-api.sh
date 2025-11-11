#!/bin/bash

# 微信公众号草稿创建接口测试脚本

# 请替换为实际的access_token
ACCESS_TOKEN=""

# 发送POST请求创建草稿
curl -X POST \
  "http://localhost:8080/api/wechat/draft?accessToken=$ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "articles": [
        {
            "title": "我是标题",
            "author": "我是作者",
            "digest": "我是描述简介",
            "content": "我是文章正文",
            "thumb_media_id": "1111"
        }
    ]
}'