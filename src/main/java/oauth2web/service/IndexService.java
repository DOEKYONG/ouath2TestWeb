package oauth2web.service;

import oauth2web.Entity.MemberEntity;
import oauth2web.Entity.MemberRepository;
import oauth2web.Entity.Role;
import oauth2web.dto.LoginDto;
import oauth2web.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IndexService implements UserDetailsService { //UserDetailsService 인터페이스 상속  @OVerride필요
@Autowired
    private MemberRepository memberRepository;

    // 회원가입
    public void signup (MemberDto memberDto) {
        memberRepository.save(memberDto.toentity()); // db에 toentity값들들저장
    }
    // 로그인

    @Override //  추상메소드 구현
    public UserDetails loadUserByUsername(String mid) throws UsernameNotFoundException {

        MemberEntity memberEntity = memberRepository.findBymid(mid).get(); // memberEntity에 memberRepository에서 찾은 mid값 넣기

        List<GrantedAuthority> authorities = new ArrayList<>(); // 부여된 인증들 저장
        authorities.add(new SimpleGrantedAuthority(memberEntity.getRole().getKey())); // 권한 추가, 세션 부여??
       // LoginDto loginDto = new LoginDto(mid," qwqw",authorities);
//
//        System.out.println(memberEntity);
//        System.out.println(authorities);

        return new LoginDto(memberEntity,authorities); // LoginDto 반환
    }


}
