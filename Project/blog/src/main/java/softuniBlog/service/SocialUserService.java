package softuniBlog.service;



import org.springframework.social.facebook.api.User;


public interface SocialUserService {

    void registerOrLogin(User facebookUser);

    void registerOrLogin(softuniBlog.entity.User faceboookUser);
}
