package com.example.demo.configuration;

import nl.altindag.ssl.SSLFactory;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

@Configuration
@ConfigurationProperties(prefix = "demo")
public class TlsConfiguration {

    @Autowired
    protected DemoConfiguration demoConfiguration;

    @Bean
    public SSLContext getSSLContext() {
        final SSLFactory sslFactory = SSLFactory.builder()
                .withIdentityMaterial(demoConfiguration.getClientKeyManager())
                .withTrustMaterial(demoConfiguration.getServerTrustManager())
                .build();

        return sslFactory.getSslContext();
    }

    @Bean
    public SSLConnectionSocketFactory configureSSLContext(SSLContext sslContext) {
        return SSLConnectionSocketFactoryBuilder.create()
                .setHostnameVerifier(NoopHostnameVerifier.INSTANCE) // To accept localhost
                .setSslContext(sslContext)
                .build();
    }

    @Bean
    public RestTemplate configContext(SSLConnectionSocketFactory sslSocketFactory) {
        final PoolingHttpClientConnectionManager connManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslSocketFactory)
                .build();

        final CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connManager)
                .build();

        final var httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setHttpClient(httpClient);

        final var restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(httpRequestFactory);

        return restTemplate;
    }

}
