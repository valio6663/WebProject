package softuniBlog.repository;

import softuniBlog.entity.SocialUser;

public interface SocialUserRepository extends UserRepository<SocialUser>{

    SocialUser findOneByUserName(String email);
}
