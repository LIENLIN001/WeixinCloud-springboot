#!/bin/bash

# 微信公众号文章发布接口测试脚本

# 请替换为实际的access_token
ACCESS_TOKEN="-"

# 发送POST请求发布文章
curl -X POST \
  "http://localhost:8080/api/wechat/publish?accessToken=$ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "media_id": ""
}'