package lk.apiit.intelligent_article_generator.Article.Entity;

import lk.apiit.intelligent_article_generator.Auth.Entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "user_article" ,schema = "public")
public class UserArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User artucleUser;
}
