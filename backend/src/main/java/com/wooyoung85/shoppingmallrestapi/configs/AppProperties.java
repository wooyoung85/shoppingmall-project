package com.wooyoung85.shoppingmallrestapi.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;

@Component
@ConfigurationProperties(prefix = "shoppingmall-init")
@Getter @Setter
public class AppProperties {

    @NotEmpty
    private String adminEmail;

    private String adminName;

    @NotEmpty
    private String adminPassword;

    @NotEmpty
    private String userEmail;

    private String userName;

    @NotEmpty
    private String userPassword;

    @NotEmpty
    private String clientId;

    @NotEmpty
    private String clientSecret;

}
