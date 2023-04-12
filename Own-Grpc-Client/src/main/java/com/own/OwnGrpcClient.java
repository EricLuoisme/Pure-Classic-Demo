package com.own;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OwnGrpcClient {
    public static void main(String[] args) {
        SpringApplication.run(OwnGrpcClient.class, args);
    }
}
