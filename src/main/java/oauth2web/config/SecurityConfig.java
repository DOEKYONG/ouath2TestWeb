package oauth2web.config;

import oauth2web.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests().antMatchers("/").permitAll() // 인증없이 모든 접근 허용
                .and()
                .formLogin()// 로그인 페이지 보안 설정
                .loginPage("/member/login")// 로그인 페이지 url
                .loginProcessingUrl("/member/logincontroller")// 로그인 처리 url 생성
                .usernameParameter("mid") // 로그인 아이디 받기
                .passwordParameter("mpassword") // 로그인 패스워드 받기
                .failureUrl("/member/login/error") // 로그인 실패시 연결될 url
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout")) // 로그아웃 처리할 url 생성
                .logoutSuccessUrl("/") // 로그아웃 성공시 연결될 페이지
                .invalidateHttpSession(true) // 세션 초기화
                .and()
                .csrf() // csrf : 사이트 간 요청 위조 [ 해킹 공격 방법중 하나 ] = 서버에게 요청할수 있는 페이지 제한
                //csrf 예외처리
                .ignoringAntMatchers("/member/logincontroller")
                .ignoringAntMatchers("/member/signupcontroller");
    }
    @Autowired
    private IndexService indexService;

    @Override // 인증(로그인) 관리 메소드
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(indexService).passwordEncoder(new BCryptPasswordEncoder());
                // 인증할 서비스객체                  -> 패스워드 인코딩(   BCrypt 객체로  )
    }
}
