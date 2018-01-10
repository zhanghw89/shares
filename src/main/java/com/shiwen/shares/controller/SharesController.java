package com.shiwen.shares.controller;

import com.shiwen.shares.entity.ShareEntity;
import com.shiwen.shares.util.HttpClientUtils;
import com.shiwen.shares.util.SendSMS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import static com.shiwen.shares.entity.ShareEntity.buildShareEntity;

@RestController
@RequestMapping("/shares")
@Slf4j
public class SharesController {

    private String shenZhouChangCheng = "sz000018";
    private String shenZhouXinXi = "sz000555";



    private String baseUrl = "http://hq.sinajs.cn/list=";


    @GetMapping
    public ShareEntity getShareByCode(String shareCode) throws Exception {

        String result = HttpClientUtils.getInstance().getUrl(baseUrl+shenZhouChangCheng, new HashMap<String, String>());
        String result2 = HttpClientUtils.getInstance().getUrl(baseUrl+shenZhouXinXi, new HashMap<String, String>());

        ShareEntity shareEntity = buildShareEntity(result);
        ShareEntity shareEntity2 = buildShareEntity(result2);

        double price =6.20;
        if(Double.valueOf(shareEntity.getCurrentPrice()) > price){
            SendSMS.send(String.format("神州长城当前的价格是:%s ,大于您的预定价格:%s",shareEntity.getCurrentPrice(),price));
        }

        return shareEntity;

    }


}
