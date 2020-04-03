package lk.apiit.intelligent_article_generator.Article.Repository;

import lk.apiit.intelligent_article_generator.Article.Entity.UserArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserArticleRepository extends JpaRepository<UserArticle,Long> {

    List<UserArticle> getAllByArtucleUser_UserId(long id);
}
