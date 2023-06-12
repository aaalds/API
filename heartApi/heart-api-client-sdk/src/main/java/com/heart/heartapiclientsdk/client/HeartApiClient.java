package com.heart.heartapiclientsdk.client;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.heart.heartapiclientsdk.model.BaiduParams;
import com.heart.heartapiclientsdk.model.WeatherParams;
import com.heart.heartapiclientsdk.model.AvatarParams;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.heart.heartapiclientsdk.utils.SignUtils.getSign;

/**
 * 调用第三方的客户端
 *
 * @ClassName HeartApiClient
 * @Description TODO
 * @Author OTTO
 * @Date 2022/11/30 20:52
 */
public class HeartApiClient {

    private String GATEWAY_HOST = "http://localhost:8090";

    public void setGATEWAY_HOST(String gateway_Host){
        this.GATEWAY_HOST=gateway_Host;
    }

    private String accessKey;

    private String secretKey;


    public HeartApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }



    private Map<String, String> getHeaderMap(String body) {
        String encode=null;
        try {
            encode = URLEncoder.encode(body, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        Map<String, String> hasMap = new HashMap<>();
        String nonce = RandomUtil.randomNumbers(4);
        String currentTime = String.valueOf(System.currentTimeMillis() / 1000);
        hasMap.put("accessKey", accessKey);
        hasMap.put("nonce", nonce);
        hasMap.put("body", encode);
        hasMap.put("timestamp", currentTime);
        hasMap.put("sign", getSign(encode, secretKey,nonce,currentTime));
        return hasMap;
    }



    public String getWeatherInfo(WeatherParams weatherParams){

        String parameters = JSON.toJSONString(weatherParams);
        String result = onlineInvoke(parameters, "/api/weather/weatherInfo");
        return result;
    }

    public String getAvatarUrl(AvatarParams avatarParams){
        String parameters = JSON.toJSONString(avatarParams);
        String result = onlineInvoke(parameters, "/api/avatar/avatarUrl");
        return result;
    }

    public String getBaiduInfo(BaiduParams baiduParams){
        String parameters = JSON.toJSONString(baiduParams);
        String result = onlineInvoke(parameters, "/api/baidu/baiduInfo");
        return result;
    }

    public String onlineInvoke(String parameters,String url) {
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + url)
                .addHeaders(getHeaderMap(parameters))
                .body(parameters)
                .execute();
        System.out.println(httpResponse.getStatus());
        String result = httpResponse.body();
        return result;
    }
}
