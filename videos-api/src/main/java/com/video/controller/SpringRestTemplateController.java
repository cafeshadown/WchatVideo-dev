package com.video.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.video.cofig.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@RestController
public class SpringRestTemplateController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(value = "/testPost")
    public Object postJson() {
        String url = "https://aip.baidubce.com/rest/2.0/antispam/v2/spam?access_token=" + AuthService.getAuth();
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("content", "你是傻逼吗");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<MultiValueMap<String, Object>> r = new HttpEntity<>(postParameters, headers);

        String data= restTemplate.postForObject(url, r, String.class);
        System.out.println(data);

        JSONObject jsonObject=JSONObject.parseObject(data);
        jsonObject=JSON.parseObject(jsonObject.get("result").toString());
        jsonObject=JSON.parseObject(jsonObject.get("reject").toString());
        System.out.println(jsonObject.get("label"));
        return null;

  }
}
