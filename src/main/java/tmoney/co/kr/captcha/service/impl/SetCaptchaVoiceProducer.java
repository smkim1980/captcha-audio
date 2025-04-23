package tmoney.co.kr.captcha.service.impl;

import lombok.extern.slf4j.Slf4j;
import nl.captcha.audio.Sample;
import nl.captcha.audio.producer.VoiceProducer;
import nl.captcha.util.FileUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static tmoney.co.kr.captcha.util.CaptchaUtil.getCaptchaMap;


@Component
@Slf4j
public class SetCaptchaVoiceProducer implements VoiceProducer {

    private static final Map<Integer, String> DEFAULT_VOICES_MAP;

    static {
        DEFAULT_VOICES_MAP = new HashMap<>();

        StringBuilder sb;
        for (int i = 0; i < 10; i++) {
            sb = new StringBuilder("/sounds/ko/numbers/");
            sb.append(i);
            sb.append(".wav");
            DEFAULT_VOICES_MAP.put(i, sb.toString());
        }
        int j = 10;
        for (char c = 'A'; c <= 'Z'; c++) {
            sb = new StringBuilder("/sounds/en/alphabet/");
            sb.append(c);
            sb.append(".wav");
            DEFAULT_VOICES_MAP.put(j, sb.toString());
            j++;
        }
    }

    private final Map<Integer, String> _voices;

    public SetCaptchaVoiceProducer() {
        this(DEFAULT_VOICES_MAP);
    }


    public SetCaptchaVoiceProducer(Map<Integer, String> voices) {
        _voices = voices;
    }

    @Override
    public Sample getVocalization(char input) {
        int idx = -1;
        if (!Character.isDigit(input)) {
            String voiceStr = String.valueOf(input).toUpperCase();
            Map<String, Integer> captchaMap = getCaptchaMap();
            if (captchaMap.containsKey(voiceStr)) {
                idx = captchaMap.get(voiceStr);
            }
        } else {
            idx = Integer.parseInt(input + "");
        }

        String filename = _voices.get(idx);
        return FileUtil.readSample(filename);

    }
}
