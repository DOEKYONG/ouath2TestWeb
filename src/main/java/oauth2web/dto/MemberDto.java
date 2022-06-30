package oauth2web.dto;

import lombok.*;
import oauth2web.Entity.MemberEntity;
import oauth2web.Entity.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MemberDto {
    private int mno;
    private String mid;
    private String mpassword;

    public MemberEntity toentity(){ // 빌더를 사용해 dto를 엔티티로 반환 (db에 dto 저장 불가)
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); // 패스워드 암호화
        return  MemberEntity.builder()
                .mid(this.mid)
                .mpassword(encoder.encode(this.mpassword))
                .role(Role.Member)
                .build();
    }
}
