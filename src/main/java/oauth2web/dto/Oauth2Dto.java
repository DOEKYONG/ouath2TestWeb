package oauth2web.dto;

import lombok.*;
import oauth2web.Entity.MemberEntity;
import oauth2web.Entity.Role;

import java.util.Map;
@Getter@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Oauth2Dto {

    private String mid;
    private String registrationid;
    private Map<String, Object> attributes;
    private String nameAttributeKey;

    // 네이버 로그인인지 카카오 로그인인지 판단하는 메소드
    public static Oauth2Dto sns확인(String registrationid, Map<String, Object> attributes, String nameAttributeKey) {
        if (registrationid.equals("naver")) { // 클라이언트 등록 이름이 naver 이면
            return naver(registrationid, attributes, nameAttributeKey);
        } else if (registrationid.equals("kakao")) {   // 클라이언트 등록 이름이 kakao 이면
            return kakao(registrationid, attributes, nameAttributeKey);
        } else {
            return null;
        }
    }

    // 네이버 로그인시
    public static Oauth2Dto naver(String registrationid, Map<String, Object> attributes, String nameAttributeKey) {
        Map<String, Object> response = (Map<String, Object>) attributes.get(nameAttributeKey);
        // 네이버 로그인시 response
        return Oauth2Dto.builder()
                .mid((String) response.get("email"))    // 이메일을 아이디로 사용
                .build();
    }

    // 카카오 로그인시
    public static Oauth2Dto kakao(String registrationid, Map<String, Object> attributes, String nameAttributeKey) {
        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get(nameAttributeKey);
        // 카카오 로그인시 kakao_account
        return Oauth2Dto.builder()
                .mid((String) kakao_account.get("email"))   // 이메일을 아이디로 사용
                .build();
    }

    // dto -> entity 변환
    public MemberEntity toentity() {
        return MemberEntity.builder()
                .mid(this.mid)
                .role(Role.sns)  // oauth 로그인시 role에 SNS사용자 저장
                .build();
    }
}


