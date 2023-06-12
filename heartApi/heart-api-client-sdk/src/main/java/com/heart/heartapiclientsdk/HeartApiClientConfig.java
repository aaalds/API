package com.heart.heartapiclientsdk;

import com.heart.heartapiclientsdk.client.HeartApiClient;
import com.heart.heartapiclientsdk.model.User;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName HeartApiClientConfig
 * @Description TODO
 * @Author OTTO
 * @Date 2022/12/1 23:26
 */

@Configuration
@Data
@ComponentScan
@ConfigurationProperties("heartapi.client")
public class HeartApiClientConfig {

    private String accessKey;

    private String secretKey;

    @Bean
    public HeartApiClient heartApiClient(){
        return new HeartApiClient(accessKey, secretKey);
    }

}
