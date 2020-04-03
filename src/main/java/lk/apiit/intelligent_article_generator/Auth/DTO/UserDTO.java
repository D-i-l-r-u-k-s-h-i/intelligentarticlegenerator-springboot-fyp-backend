package lk.apiit.intelligent_article_generator.Auth.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private long customerId;

    private String customerFirstName;

    private String customerLastName;

    private String customerContactNo;

    private String customerEmergencyContactNo;

//    private String customerAddress;

    private String customerStatus;

    private String customerUserName;

    private String customerPassword;

    private String confirmPassword;

    private String customerEmail;

    private boolean isBlacklisted;

    private long roleId;
}
