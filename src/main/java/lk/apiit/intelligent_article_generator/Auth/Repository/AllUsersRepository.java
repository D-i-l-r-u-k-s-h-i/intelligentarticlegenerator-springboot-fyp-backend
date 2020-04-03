package lk.apiit.intelligent_article_generator.Auth.Repository;

import lk.apiit.intelligent_article_generator.Auth.Entity.AllUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllUsersRepository extends JpaRepository<AllUsers, Long> {
    AllUsers findByUsername(String Username);
    AllUsers findAllUsersById(long id);
}
