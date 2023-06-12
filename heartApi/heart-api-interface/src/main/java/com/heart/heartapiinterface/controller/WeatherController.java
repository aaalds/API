package com.heart.heartapiinterface.controller;

import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.heart.heartapiclientsdk.model.WeatherParams;
import com.heart.heartapiinterface.domain.Country;
import com.heart.heartapiinterface.mapper.CountryMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName WeatherController
 * @Description TODO
 * @Author lish
 * @Date 2023/5/1 17:01
 */

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Resource
    private CountryMapper countryMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/weatherInfo")
    public String getWeatherInfoByPost(@RequestBody WeatherParams weatherParams, HttpServletRequest request){
        String city = weatherParams.getCity();
        String extensions = weatherParams.getExtensions();
        String weatherInfo = stringRedisTemplate.opsForValue().get(city+extensions);
        if(StringUtils.isNotBlank(weatherInfo)){
            return weatherInfo;
        }
        LambdaUpdateWrapper<Country> lqw = new LambdaUpdateWrapper<>();
        lqw.eq(Country::getCountry, city);
        Country country = countryMapper.selectOne(lqw);
        Integer adcode = country.getAdcode();
        //https://restapi.amap.com/v3/weather/weatherInfo?
        String weatherUrl="https://restapi.amap.com/v3/weather/weatherInfo";
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("key", "09fd9a3533d944a84d98f264ad0a841d");
        paramMap.put("city",adcode);
        paramMap.put("extensions", extensions);
        String result = HttpUtil.get(weatherUrl, paramMap);
        stringRedisTemplate.opsForValue().set(city+extensions,result,10, TimeUnit.MINUTES);
        return result;
    }
}
