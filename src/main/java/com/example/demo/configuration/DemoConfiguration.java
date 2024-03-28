package com.example.demo.configuration;

import lombok.Getter;
import lombok.Setter;
import nl.altindag.ssl.pem.util.PemUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509ExtendedTrustManager;
import java.nio.file.Path;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "demo")
public class DemoConfiguration {
    public String clientCrt;
    public String clientKey;
    public String serverCrt;

    public Path getClientCrtPath() {
        return Path.of(clientCrt);
    }

    public Path getClientKeyPath() {
        return Path.of(clientKey);
    }

    public Path getServerCrtPath() {
        return Path.of(serverCrt);
    }

    public X509ExtendedKeyManager getClientKeyManager() {
        return PemUtils.loadIdentityMaterial(getClientCrtPath(), getClientKeyPath());
    }

    public X509ExtendedTrustManager getServerTrustManager() {
        return PemUtils.loadTrustMaterial(getServerCrtPath());
    }
}
