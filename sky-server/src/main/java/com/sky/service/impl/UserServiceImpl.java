package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatProperties weChatProperties;
    public static final String URL = "https://api.weixin.qq.com/sns/jscode2session";
    /**
     * 微信用户登录
     * @param userLoginDTO
     * @return
     */
    @Override
    @Transactional
    public User getWechatUser(UserLoginDTO userLoginDTO) {
        //1.调用微信接口服务，得到openid
        Map<String, String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",userLoginDTO.getCode());
        map.put("grant_type","authorization_code");
        //向微信服务发送请求
        String json = HttpClientUtil.doGet(URL, map);
        //将请求到的json对象转为java对象
        JSONObject jsonObject = JSONObject.parseObject(json);
        String openid =  jsonObject.getString("openid");
        //2.判断openid是否为空，为空则微信登录失败
        if(openid==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //3.不为空，如果用户为新用户，为用户进行注册
        User user =userMapper.query(openid);
        if(user==null){
            user = new User();
            user.setOpenid(openid);
            user.setCreateTime(LocalDateTime.now());
            userMapper.insert(user);
        }
        return user;
    }
}
