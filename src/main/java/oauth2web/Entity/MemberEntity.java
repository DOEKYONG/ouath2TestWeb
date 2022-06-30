package oauth2web.Entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@ToString
@Builder
public class MemberEntity {

    @Id // pk 지정 어노테이션
    @GeneratedValue(strategy = GenerationType.AUTO) // 자동키 생성
    private int mno;
    private String mid;
    private String mpassword;
    @Enumerated( EnumType.STRING ) // enum이름을 저장
    private Role role;

}
