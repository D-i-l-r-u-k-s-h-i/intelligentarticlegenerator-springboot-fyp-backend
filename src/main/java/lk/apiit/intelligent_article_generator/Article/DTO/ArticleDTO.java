package lk.apiit.intelligent_article_generator.Article.DTO;

import lk.apiit.intelligent_article_generator.Article.Entity.ArticleStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ArticleDTO {
    private long id;

    private String articleName;

    private byte[] articleFile;

    private ArticleStatus articleStatus;

    private String dateTime;

    private Date createdDate;

    private Date lastModifiedDate;

    private String articleData;
}
