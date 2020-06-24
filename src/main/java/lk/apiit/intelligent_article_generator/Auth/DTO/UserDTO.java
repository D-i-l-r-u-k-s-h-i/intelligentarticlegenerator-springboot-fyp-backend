package lk.apiit.intelligent_article_generator.Auth.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private long userId;

    private String userFName;

    private String userLName;

    private String userUserName;

    private String password;

    private String confirmPassword;

    private String userEmail;

    private long roleId;
}
