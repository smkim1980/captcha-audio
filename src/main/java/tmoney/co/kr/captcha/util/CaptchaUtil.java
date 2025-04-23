package tmoney.co.kr.captcha.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CaptchaUtil {

    public static Map<String, Integer> getCaptchaMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("A", 10);
        map.put("B", 11);
        map.put("C", 12);
        map.put("D", 13);
        map.put("E", 14);
        map.put("F", 15);
        map.put("G", 16);
        map.put("H", 17);
        map.put("I", 18);
        map.put("J", 19);
        map.put("K", 20);
        map.put("L", 21);
        map.put("M", 22);
        map.put("N", 23);
        map.put("O", 24);
        map.put("P", 25);
        map.put("Q", 26);
        map.put("R", 27);
        map.put("S", 28);
        map.put("T", 29);
        map.put("U", 30);
        map.put("V", 31);
        map.put("W", 32);
        map.put("X", 33);
        map.put("Y", 34);
        map.put("Z", 35);
        return map;
    }

}
