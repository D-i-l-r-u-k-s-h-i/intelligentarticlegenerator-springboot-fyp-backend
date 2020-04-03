package lk.apiit.intelligent_article_generator.Article.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "article_status" ,schema = "public")
public class ArticleStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long statusId;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private StatusName statusName;

    public ArticleStatus(StatusName statusName) {
    }
}
