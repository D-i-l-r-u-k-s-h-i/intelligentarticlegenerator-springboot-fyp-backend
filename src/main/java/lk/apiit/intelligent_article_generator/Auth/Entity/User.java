package lk.apiit.intelligent_article_generator.Auth.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "user" ,schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    private String userFName;

    private String userLName;

//    private String userContactNo;

    private String userUserName;

    private String userEmail;

    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id",nullable = false)
    private AllUsers user;


//    @ManyToOne
//    @JoinColumn(name = "role_id",nullable = false)
//    private Role role;
}
