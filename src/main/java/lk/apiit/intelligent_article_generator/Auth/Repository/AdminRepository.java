package lk.apiit.intelligent_article_generator.Auth.Repository;

import lk.apiit.intelligent_article_generator.Auth.Entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Long> {
    Admin getByUsername(String username);
}
