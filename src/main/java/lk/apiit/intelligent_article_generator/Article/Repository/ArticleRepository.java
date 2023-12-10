package lk.apiit.intelligent_article_generator.Article.Repository;

import lk.apiit.intelligent_article_generator.Article.Entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {
    Article findByArticleId(long id);

    @Query("SELECT u FROM Article u WHERE lower(u.articleName) like lower(concat('%', ?1,'%')) or lower(u.dateTime) like lower(concat('%', ?1,'%'))")
    List<Article> searchArticle(String name);
}
