package lk.apiit.intelligent_article_generator.Auth.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDTO {

    private String username;

    private String password;

    private String email;

    private String contactNo;

    private int role;

    private String currentAdminPass;
}
