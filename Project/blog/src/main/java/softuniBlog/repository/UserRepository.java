package softuniBlog.repository;



import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@NoRepositoryBean
public interface UserRepository<T extends User> extends CrudRepository<T, Long> {

    T findOneByUsername(String username);

    void saveAndFlush(User user);
}