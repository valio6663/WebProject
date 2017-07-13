package softuniBlog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import softuniBlog.entity.Article;

import java.awt.*;

public interface ArticleRepository extends JpaRepository<Article , Integer> {
    Page<Article> findAll(Pageable pageable);
}
