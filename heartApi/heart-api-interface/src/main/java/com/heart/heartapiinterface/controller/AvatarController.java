package com.heart.heartapiinterface.controller;

import cn.hutool.http.HttpUtil;
import com.heart.heartApiCommon.model.entity.BaiduParams;
import com.heart.heartapiclientsdk.model.AvatarParams;
import com.heart.heartapiinterface.utils.SslUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * @ClassName AvatarController
 * @Description TODO
 * @Author lish
 * @Date 2023/5/9 0:37
 */
@RestController
@RequestMapping("/avatar")
public class AvatarController {


    @PostMapping("/avatarUrl")
    public String getAvatarUrlByPost(@RequestBody(required = false) AvatarParams avatarParams, HttpServletRequest request) {
        //https://restapi.amap.com/v3/weather/weatherInfo?
        String avatarUrl = "http://yichen.api.z7zz.cn/api/sjtx2.php";
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("type", "json");
        paramMap.put("form", avatarParams.getForm());
        String result = HttpUtil.get(avatarUrl, paramMap);
        return result;
    }

}
