package lk.apiit.intelligent_article_generator.Article.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long articleId;

    private byte[] articleFile;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private ArticleStatus articleStatus;

    private String dateTime;

}
