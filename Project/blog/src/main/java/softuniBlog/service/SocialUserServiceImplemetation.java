package softuniBlog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import softuniBlog.entity.SocialUser;
import softuniBlog.entity.User;
import softuniBlog.repository.SocialUserRepository;
import softuniBlog.repository.SocialUserRepository;

@Service
public class SocialUserServiceImplemetation implements SocialUserService{
    @Autowired
    private SocialUserRepository socialUserRepository;
    private RoleService roleService;

    @Override
    public void registerOrLogin(User faceboookUser) {
        String email = faceboookUser.getEmail();
        SocialUser socialUser = this.socialUserRepository.findOneByUserName(email);

        if (socialUser == null){
            registerUser(email);
        }
        else {
            loginUser(socialUser);
        }
    }
    private SocialUser registerUser(String email) {
        SocialUser user = new SocialUser();
        user.setUsername(email);
        user.setProvider("FACEBOOK");
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.addRole(this.roleService.getDefaultRole());
        this.socialUserRepository.save(user);
        return user;
    }

    private void loginUser(SocialUser socialUser) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(socialUser.getUsername(), null, socialUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
