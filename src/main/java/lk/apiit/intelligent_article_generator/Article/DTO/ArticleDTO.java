package lk.apiit.intelligent_article_generator.Article.DTO;

import lk.apiit.intelligent_article_generator.Article.Entity.ArticleStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleDTO {
    private long id;

    private byte[] articleFile;

    private ArticleStatus articleStatus;

    private String dateTime;
}
