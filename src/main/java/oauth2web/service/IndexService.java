package oauth2web.service;

import oauth2web.Entity.MemberEntity;
import oauth2web.Entity.MemberRepository;
import oauth2web.Entity.Role;
import oauth2web.dto.LoginDto;
import oauth2web.dto.MemberDto;
import oauth2web.dto.Oauth2Dto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IndexService implements UserDetailsService , OAuth2UserService<OAuth2UserRequest, OAuth2User> { //UserDetailsService 인터페이스 상속  @OVerride필요
@Autowired
    private MemberRepository memberRepository;

    // 회원가입
    public void signup (MemberDto memberDto) {
        memberRepository.save(memberDto.toentity()); // db에 toentity값들들저장
    }
    // 로그인

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException { // oAouth 로그인 추상메소드 구현
        // 인증[로그인] 결과 정보 요청
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser( userRequest );

        // 클라이언트 아이디 oauth 구분용
        String registrationid = userRequest.getClientRegistration().getRegistrationId();
        // 회원정보 요청시 사용되는 JSON 키 이름 호출
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String nameAttributeKey =
                userRequest.getClientRegistration()
                        .getProviderDetails()
                        .getUserInfoEndpoint()
                        .getUserNameAttributeName();

        // oauth2 정보 -> Dto -> entitiy -> db저장
        Oauth2Dto oauth2Dto = Oauth2Dto.sns확인(registrationid,attributes,nameAttributeKey);

        // 1. 로그인 한 이메일로 엔티티호출
        Optional<MemberEntity> optional
                =  memberRepository.findBymid( oauth2Dto.getMid());
        // 2. 만약에 엔티티가 없으면
        if( !optional.isPresent() ){
            memberRepository.save( oauth2Dto.toentity() );  // entity 저장
        }

//        System.out.println("회원정보(json) 호출시 사용되는 키 : "+nameAttributeKey);
//        System.out.println("인증 정보(로그인) 결과 내용 : "+oAuth2User.getAttributes());


        // 반환타입 DefaultOAuth2User ( 권한(role)명 , 회원인증정보 , 회원정보 호출키 )
        // DefaultOAuth2User , UserDetails : 반환시 인증세션 자동 부여 [ SimpleGrantedAuthority : (권한) 필수~  ]
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("SNS사용자")),
                attributes ,
                nameAttributeKey
        );




    }

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


    public  String 인증된회원아이디호출(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String mid = null;
        if(principal !=null) {
            if(principal instanceof UserDetails) {
                mid = ((UserDetails) principal).getUsername();
            } else if (principal instanceof OAuth2User){
                Map<String,Object> attributes = ((OAuth2User)principal).getAttributes();
                if(attributes.get("response") != null) {
                    Map<String ,Object> map = (Map<String,Object>) attributes.get("response");
                    mid = map.get("email").toString();
                }else if(attributes.get("kakao_account")!= null) {
                    Map<String,Object> map = (Map<String,Object>) attributes.get("kakao_account");
                    mid = map.get("email").toString();
                } else {

                }
            }

        }else {
            return null;
        }
        return mid;
    }


}
