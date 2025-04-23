package tmoney.co.kr.captcha.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import tmoney.co.kr.captcha.model.LoginResponse;
import tmoney.co.kr.captcha.service.AudioCaptchaService;
import tmoney.co.kr.captcha.util.CryptoUtil;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;


@Controller
@Slf4j
public class LoginController {
    @Autowired
    private DefaultKaptcha kaptchaProducer;

    @Autowired
    private AudioCaptchaService audioCaptchaService;

    @GetMapping("/login")
    public ModelAndView loginPage(ModelAndView modelAndView) throws Exception {
        modelAndView.addObject("encodedKey", CryptoUtil.generateKey());
        modelAndView.setViewName("login");
        return modelAndView;


    }

    @GetMapping("/success")
    public String successPage() {
        return "success";
    }

    @GetMapping("/captcha-image")
    public void getCaptchaImage(HttpSession session, HttpServletResponse response) throws IOException {
        try {
            // 응답 설정
            response.setContentType("image/jpeg");
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

            // Captcha 코드 생성 및 세션에 저장
            String code = kaptchaProducer.createText();
            log.info("code={}", code);
            session.setAttribute("kaptchaCode", code);

            // Captcha 이미지 생성
            BufferedImage image = kaptchaProducer.createImage(code);

            // 이미지 출력
            ImageIO.write(image, "jpg", response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error generating captcha image: " + e.getMessage());
        }
    }

    @GetMapping(value = "/audio", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public void getCaptchaAudio(HttpServletRequest request, HttpServletResponse response) throws Exception {
        audioCaptchaService.captchaAudio(request, response);
    }


    @PostMapping("/authenticate")
    @ResponseBody
    public LoginResponse authenticate(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String captchaInput,
            @RequestParam String encodedKey,
            HttpSession session) throws Exception {
        String kaptchaCode = (String) session.getAttribute("kaptchaCode");
        log.info("captchaInput = {},kaptchaCode = {}", captchaInput, kaptchaCode);

        log.info("password = {},dec password = {}", password, CryptoUtil.decrypt(encodedKey, password));

        if (captchaInput != null && captchaInput.equals(kaptchaCode)) {
            // 인증 로직
            return new LoginResponse(true, "로그인 성공");
        } else {
            return new LoginResponse(false, "Captcha가 일치하지 않습니다.");
        }
    }

//    @PostMapping("/authenticate")
//    @ResponseBody
//    public LoginResponse authenticate(
//            @RequestBody LoginRequest loginRequest,
//            HttpSession session) throws Exception {
//        String kaptchaCode = (String) session.getAttribute("kaptchaCode");
//        log.info("captchaInput = {},kaptchaCode = {}", loginRequest.getCaptchaInput(), kaptchaCode);
//
//        log.info("password = {},dec password = {}", loginRequest.getPassword(), CryptoUtil.decrypt(loginRequest.getPassword()));
//
//        if (loginRequest.getCaptchaInput() != null && loginRequest.getCaptchaInput().equals(kaptchaCode)) {
//            // 인증 로직
//            return new LoginResponse(true, "로그인 성공");
//        } else {
//            return new LoginResponse(false, "Captcha가 일치하지 않습니다.");
//        }
//    }
}
