package com.example.demo.configuration;

import nl.altindag.ssl.SSLFactory;
import nl.altindag.ssl.pem.util.PemUtils;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@Configuration
public class TlsConfiguration {

    @Bean
    public SSLConnectionSocketFactory configureSSLContext() {
        // Load PEM key and certificate
        File pemCertFile = new File("D:\\Repositorios\\projeto-mtls\\nginx\\certs\\clients\\certificado-do-cliente.crt");
        File pemKeyFile = new File("D:\\Repositorios\\projeto-mtls\\nginx\\certs\\clients\\certificado-do-cliente.key");
        File serverCertFile = new File("D:\\Repositorios\\projeto-mtls\\nginx\\certs\\localhost.crt");

        final var keyManager = PemUtils.loadIdentityMaterial(pemCertFile.toPath(), pemKeyFile.toPath());
        final var trustManager = PemUtils.loadTrustMaterial(serverCertFile.toPath());

        final SSLFactory sslFactory = SSLFactory.builder()
                .withIdentityMaterial(keyManager)
                .withTrustMaterial(trustManager)
                .build();

        return SSLConnectionSocketFactoryBuilder.create()
                .setSslContext(sslFactory.getSslContext())
                .setHostnameVerifier(NoopHostnameVerifier.INSTANCE) // To accept localhost
                .build();
    }

    @Bean
    public RestTemplate configContext(SSLConnectionSocketFactory sslSocketFactory) throws UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException {
        final PoolingHttpClientConnectionManager connManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslSocketFactory)
                .build();

        final CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connManager)
                .build();

        final var httpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        return new RestTemplate(httpRequestFactory);
    }

}
