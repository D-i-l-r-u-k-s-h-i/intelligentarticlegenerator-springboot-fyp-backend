package lk.apiit.intelligent_article_generator.Article.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Article extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long articleId;

    private String articleName;

    private byte[] articleFile;

    private byte[] htmlFile;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private ArticleStatus articleStatus;

    private String dateTime;

}
