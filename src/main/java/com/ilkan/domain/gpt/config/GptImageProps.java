package com.ilkan.domain.gpt.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "gpt.image")
public class GptImageProps {

    private String model = "gpt-image-1";

    private String size = "1024x1024";

    private int n = 1;

    private String responseFormat = "b64_json";
}
