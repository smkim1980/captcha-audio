package tmoney.co.kr.captcha.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;


@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha kaptchaProducer() {

        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width", "200");
        properties.setProperty("kaptcha.image.height", "50");
        properties.setProperty("kaptcha.textproducer.font.size", "40");
        properties.setProperty("kaptcha.textproducer.char.length", "6");
        //properties.setProperty("kaptcha.textproducer.char.string", "0123456789");

        Config config = new Config(properties);
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        kaptcha.setConfig(config);
        return kaptcha;
    }

}
