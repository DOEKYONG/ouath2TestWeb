package oauth2web.controller;

import oauth2web.dto.MemberDto;
import oauth2web.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class IndexController {
    @Autowired // bean 자동주입 => 객체생성시 new와 같은 역할
    private IndexService indexService;

    @GetMapping("/") // 경로 시작화면으로
    public String main() {
        return "main";
    }

    @GetMapping("/member/login") // 경로 member/login
    public String login() {
        return "login";
    } // login.html 을 반환

    @GetMapping("/member/signup") // 경로 member/signup
    public String signup() {
        return "signup";
    } // signup.html 을 반환

    @PostMapping("/member/signupcontroller") // post 방식 컨트롤러 경로
    public String signupcontroller(MemberDto memberDto) {
        indexService.signup(memberDto); // indexservice에서 signup 실행
        return "redirect:/"; // 처음화면으로 반환
    }


}
