package oauth2web.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {

    Member("ROLE_MEMBER","회원"),
    ADMIN("ROLE_ADMIN","관리자"),
    sns("ROLE_MEMBER","SNS사용자");

        private final String key;
        private final String keyword;
}
