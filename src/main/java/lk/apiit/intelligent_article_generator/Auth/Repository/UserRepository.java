package lk.apiit.intelligent_article_generator.Auth.Repository;

import lk.apiit.intelligent_article_generator.Auth.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserEmail(String email);
    User findByUserId(long id);

//    @Query("SELECT u FROM User u WHERE lower(u.customerFName) like lower(concat('%', ?1,'%')) or lower(u.customerLName) like lower(concat('%', ?1,'%')) or lower(u.customerUserName) like lower(concat('%', ?1,'%'))")
//    List<User> searchUser(String name);
}
