package softuniBlog.repository;
import softuniBlog.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;


public interface PhotoRepository extends JpaRepository<Photo, Integer> {
    Set<Photo> findById(Integer articleId);
}
