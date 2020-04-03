package lk.apiit.intelligent_article_generator.Auth.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    private String username;

    private String password;
}
