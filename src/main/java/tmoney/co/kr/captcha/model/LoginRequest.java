package tmoney.co.kr.captcha.model;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;
    private String password;
    private String captchaInput;
}
