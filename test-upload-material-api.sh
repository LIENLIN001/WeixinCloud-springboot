#!/bin/bash

# 微信公众号上传永久素材接口测试脚本

# 请替换为实际的access_token
ACCESS_TOKEN="-17xYFXVCobmaTkNKKfvSRqPcBp1IFJsHQIdAJAXGY"

# 创建一个测试视频文件（如果不存在）
#if [ ! -f "test_video.mp4" ]; then
 #   echo "Creating a test video file..."
    # 创建一个简单的测试文件
 #   echo "This is a test video file for uploading to WeChat" > test_video.mp4
#fi

# 发送POST请求上传永久素材
curl -X POST \
  "http://localhost:8080/api/wechat/material/upload?accessToken=$ACCESS_TOKEN&type=image" \
  -F "media=@WechatIMG98.jpeg" 
  #-F "title=测试视频标题" \
  #-F "introduction=这是一个测试视频的简介"

#curl "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=$ACCESS_TOKEN&type=image" 
 # -F WechatIMG98.jpeg
/cgi-bin/material/add_material?access_token=$ACCESS_TOKEN&type=image" 
 # -F WechatIMG98.jpeg
