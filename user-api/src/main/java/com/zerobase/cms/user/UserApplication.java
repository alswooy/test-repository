package com.zerobase.cms.user;

import com.zerobase.cms.user.config.FeignConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients
@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories
@RequiredArgsConstructor

public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);


    }
}