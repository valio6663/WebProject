package softuniBlog.serviceImpl;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Service;
import softuniBlog.Errors.Errors;
import softuniBlog.bindingModel.RegistrationModel;
import softuniBlog.entity.BasicUser;
import softuniBlog.repository.BasicUserRepository;
import softuniBlog.repository.BasicUserService;
import softuniBlog.service.RoleService;

@Service
public class BasicUserServiceImpl implements BasicUserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private BasicUserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ModelMapper modelMapper;



    @Override
    public void register(RegistrationModel registrationModel) {
        BasicUser user = this.modelMapper.map(registrationModel, BasicUser.class);
        String encryptedPassword = this.bCryptPasswordEncoder.encode(registrationModel.getPassword());
        user.setPassword(encryptedPassword);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.addRole(this.roleService.getDefaultRole());
        this.userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BasicUser basicUser = this.userRepository.findOneByUsername(username);
        if(basicUser == null){
            throw new UsernameNotFoundException(Errors.INVALID_CREDENTIALS);
        }

        return (UserDetails) basicUser;
    }
}
