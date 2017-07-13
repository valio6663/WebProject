package softuniBlog.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import softuniBlog.bindingModel.RegistrationModel;
import softuniBlog.entity.Tag;

public interface TagRepository extends JpaRepository<Tag  , Integer> {

    Tag findByName(String name);

    interface BasicUserService extends UserDetailsService {

        void register(RegistrationModel registrationModel);
    }
}

