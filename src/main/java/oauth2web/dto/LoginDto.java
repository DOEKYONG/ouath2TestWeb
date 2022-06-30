package oauth2web.dto;

import lombok.Builder;
import lombok.Getter;
import oauth2web.Entity.MemberEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
public class LoginDto implements UserDetails { // UserDetails 인터페이스 상속 오버라이드필요

    private String mid;
    private String mpassword;
    private Set<GrantedAuthority> authorities;

//    public LoginDto(String mid , String mpassword, Collection<? extends GrantedAuthority> authorities) {
//        this.mid = mid;
//        this.mpassword = mpassword;
//        this.authorities = Collections.unmodifiableSet(new LinkedHashSet<>(authorities));
//    }
public LoginDto(MemberEntity memberEntity , Collection< ? extends GrantedAuthority > authorities ) { // 제네릭으로부터 상속받은 부여된 권한들을 모아놓음 ?=아무거나

    this.mid = memberEntity.getMid();
    this.mpassword = memberEntity.getMpassword();
    this.authorities = Collections.unmodifiableSet(new LinkedHashSet<>(authorities));
}


    // 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }
    // 패스워드 반환
    @Override
    public String getPassword() {
        return this.mpassword;
    }
    // 아이디 반환
    @Override
    public String getUsername() {
        return this.mid;
    }

    // 계정 인증 만료 여부 확인
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    // 계정 잠겨 있는지 확인
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    // 계정 패스워드가 만료 여부 확인
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    // 계정 사용 가능한 여부 확인
    @Override
    public boolean isEnabled() {
        return true;
    }
}
