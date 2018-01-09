package com.shiwen.shares.controller;

import com.shiwen.shares.entity.ShareEntity;
import com.shiwen.shares.util.HttpClientUtils;
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
    String url = "http://hq.sinajs.cn/list=sh601006";

    @GetMapping
    public ShareEntity getShareByCode(String shareCode) throws Exception {
        String result = HttpClientUtils.getInstance().getUrl(this.url, new HashMap<String, String>());
        return buildShareEntity(result);

    }


}
