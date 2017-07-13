package softuniBlog.repository;



import org.springframework.security.core.userdetails.UserDetailsService;
import softuniBlog.bindingModel.RegistrationModel;

import java.util.List;

public interface BasicUserService extends UserDetailsService {

    void register(RegistrationModel registrationModel);
}