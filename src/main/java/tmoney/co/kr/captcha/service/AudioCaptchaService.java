package tmoney.co.kr.captcha.service;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.captcha.audio.AudioCaptcha;
import nl.captcha.exp.SetTextProducer;
import nl.captcha.servlet.CaptchaServletUtil;
import org.springframework.stereotype.Service;
import tmoney.co.kr.captcha.service.impl.SetCaptchaVoiceProducer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Service
@Slf4j
@RequiredArgsConstructor
public class AudioCaptchaService {

    private final DefaultKaptcha kaptcha;

    private final SetCaptchaVoiceProducer setCaptchaVoiceProducer;

    public String generateCaptchaText(HttpSession session) {
        String captchaText = kaptcha.createText();
        session.setAttribute("kaptchaCode", captchaText);
        return captchaText;
    }

    public void captchaAudio(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 세션에서 캡차 코드 가져오기
        String kaptchaCode = (String) request.getSession().getAttribute("kaptchaCode");
        if (kaptchaCode == null || kaptchaCode.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        AudioCaptcha ac = new AudioCaptcha.Builder()
                .addAnswer(new SetTextProducer(kaptchaCode))
                .addVoice(setCaptchaVoiceProducer)
                .addNoise()
                .build();
        CaptchaServletUtil.writeAudio(response, ac.getChallenge());
    }

}