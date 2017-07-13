package softuniBlog.repository;

import org.springframework.stereotype.Repository;
import softuniBlog.entity.BasicUser;
import softuniBlog.entity.User;

@Repository
public interface BasicUserRepository extends UserRepository<BasicUser>{
}
