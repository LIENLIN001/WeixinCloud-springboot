#!/bin/bash

# 测试微信token接口
echo "Testing WeChat token API..."

# 发送POST请求到/api/wechat/token
curl -X POST http://localhost:8080/api/wechat/token \
  -H "Content-Type: application/json" \
  -d '{
    "appid": "111",
    "secret": "222",
    "grant_type": "client_credential"
  }'

echo -e "\nTest completed."