package softuniBlog.entity;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;
@Entity
@Table(name ="photos")
public class Photo {
    private Integer id;
    private String photo;
    private Set<Article>articles;

    public Photo() {

    }
         public Photo(String photo) {
            this.photo = photo;
            this.articles = new HashSet<>();
        }

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @Column(columnDefinition = "text", name = "photo")
        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }


        @ManyToMany(mappedBy = "photos")
        public Set<Article> getArticle() {
            return articles;
        }

        public void setArticle(Set<Article> article) {
            this.articles = article;
        }

}
