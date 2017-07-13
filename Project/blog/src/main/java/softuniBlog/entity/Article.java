package softuniBlog.entity;
import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;
import org.springframework.data.jpa.repository.JpaRepository;
import softuniBlog.entity.Tag;


@Entity
@Table(name = "articles")
public class Article {
    private Integer id;

    private String title;

    private String content;

    private User author;

    private Set<Photo>photos;

    private Set<Tag>tags;

    public Article(String title , String content , User author , Category category , Set<Photo>photos, HashSet<Tag>tags){
        this.title = title;
        this.content  = content;
        this.author = author;
        this.category = category;
        this.tags = tags;
        this.photos = photos;

    }

    @Transient
    public String getSummary(){
       return this.getContent().substring(0 , this.getContent().length() / 2 ) + "...";
    }
    public Article(String title, String content, User userEntity, Category category, HashSet<Tag> tags, Set<Photo> photos){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(columnDefinition = "text" , nullable = false)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
@ManyToOne()
@JoinColumn(nullable = false , name = "authorID")
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    private Category category;

    @ManyToOne
    @JoinColumn(nullable = false , name = "categoryId")
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @ManyToMany
    @JoinColumn(table = "articles_tags")
    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable (name = "article_photos")
    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }
}

