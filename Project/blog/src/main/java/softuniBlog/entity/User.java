package softuniBlog.entity;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public  class User  {

    private Integer id;

    private String email;

    private String fullName;

    private String password;

    private String userPhoto;

    private Set<Role> roles;

    public User() {    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "email", unique = true, nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "fullName", nullable = false)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Column(name = "password", length = 60, nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles")
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public Set<Article> articles;

    @OneToMany(mappedBy = "author")
    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }
    public User(String email, String fullName, String password , String userPhoto) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.userPhoto= userPhoto;
        this.articles = new HashSet<>();
        this.roles = new HashSet<>();
    }
    @Transient
    public boolean isAdmin(){
        return this.getRoles()
                .stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }
    @Transient
    public boolean isAuthor(Article article){
        return Objects.equals(this.getId(),article.getAuthor().getId());
    }

    String sRootPath = new File("http://localhost:8080/upload//").getAbsolutePath();

    @Column(name = "picture", nullable = true)
    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

}