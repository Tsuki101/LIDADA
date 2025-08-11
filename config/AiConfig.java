package com.lizhi.lidada.config;

import com.zhipu.oapi.ClientV4;
import lombok.Data;
import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@ConfigurationProperties(prefix = "ai")
@Data
public class AiConfig {
private String apiKey;
@Bean
    public ClientV4 clientV4(){
        return new ClientV4.Builder(apiKey).build();
}

}
