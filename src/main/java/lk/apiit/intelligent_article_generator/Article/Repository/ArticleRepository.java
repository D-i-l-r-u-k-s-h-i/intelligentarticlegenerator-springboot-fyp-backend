package lk.apiit.intelligent_article_generator.Article.Repository;

import lk.apiit.intelligent_article_generator.Article.Entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {
    Article findByArticleId(long id);
}
