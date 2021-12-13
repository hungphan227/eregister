package com.hungphan.eregister.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticConfig {

    @Value("${elastic.host}")
    private String elasticHost;

    @Value("${elastic.port}")
    private int elasticPort;

    @Bean
    public RestClient elasticClient() {
        RestClient restClient = RestClient.builder(
                new HttpHost(elasticHost, elasticPort, "http")).build();
        return restClient;
    }

}
