package softuniBlog.controller;
import javafx.scene.shape.ArcTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import softuniBlog.bindingModel.ArticleBindingModel;
import softuniBlog.entity.*;
import softuniBlog.repository.*;
import org.apache.tomcat.util.codec.binary.Base64;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import javax.jws.soap.SOAPBinding;
import javax.validation.constraints.Max;
import java.lang.management.GarbageCollectorMXBean;
import java.util.List;
import java.util.HashSet;
import java.util.stream.Collectors;
@Controller
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PhotoRepository photoRepository;

    @GetMapping("/article/create")
        @PreAuthorize("isAuthenticated()")
    public String create(Model model) {
        List<Category>categories = this.categoryRepository.findAll();
        model.addAttribute("categories" , categories);
        model.addAttribute("view", "article/create");
        return "base-layout";
    }

    @PostMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String createProcess(ArticleBindingModel articleBindingModel) throws IOException {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        User userEntity = this.userRepository.findByUserName(user.getUsername());
        Category category = this.categoryRepository.findOne(articleBindingModel.getCategoryId());
        HashSet<Tag>tags = this.findTagsFromString(articleBindingModel.getTagString());
        MultipartFile[] photosFiles = articleBindingModel.getPhoto();
        Set<Photo>photos = new HashSet<>();
        for (MultipartFile photo : photosFiles){
            if (photo.isEmpty()){
                break;
            }
            File file = new File("D:\\JavaDemoApp\\JavaDemoApp\\blog\\src\\main\\resources\\articlePhotos\\"
                    + photo.getOriginalFilename());
            photo.transferTo(file);

            String encodedPhoto = encodeFileToBase64Binary(file);

            Photo photoEntity = new Photo(encodedPhoto);

            photos.add(photoEntity);

            this.photoRepository.saveAndFlush(photoEntity);

        }
        Article articleEntity = new Article(articleBindingModel.getTitle(),
                        articleBindingModel.
                        getContent(),
                        userEntity ,
                        category,
                        tags ,
                        photos
                         );

        this.articleRepository.saveAndFlush(articleEntity);
        return "redirect:/";
    }




    @GetMapping("/article/{id}")
    public String details(Model model, @PathVariable Integer id) {
        if (!this.articleRepository.exists(id)){
            return "redirect:/";
        }


        if (!(SecurityContextHolder.getContext().getAuthentication()
        instanceof AnonymousAuthenticationToken)){
        UserDetails principal = (UserDetails)SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

            User entityUser = this.userRepository.findByUserName(principal.getUsername());
            model.addAttribute("user" , entityUser);

        }
        Article article = this.articleRepository.findOne(id);

        model.addAttribute("view" , "article/details");
        model.addAttribute("article" , article);
        return "base-layout";
    }

    @GetMapping("/article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(@PathVariable Integer id , Model model){
        if (!this.articleRepository.exists(id)){
            return "redirect:/";
        }
        Article article = this.articleRepository.findOne(id);
        String tagString = article.getTags().stream().map(Tag::getName).collect(Collectors.joining(", "));
        if (!isUserAuthorOrAdmin(article)){
            return "redirect:/article/" + id;
        }

        List<Category>categories = this.categoryRepository.findAll();
        model.addAttribute("tags" , tagString);
        model.addAttribute("categories" , categories);
        model.addAttribute("view" , "article/edit");
        model.addAttribute("article" , article);

        return "base-layout";
    }

    @PostMapping("/article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(@PathVariable Integer id , ArticleBindingModel articleBindingModel){


        if (!this.articleRepository.exists(id)){
            return "redirect:/";
        }
        Article article = this.articleRepository.findOne(id);
        if (!isUserAuthorOrAdmin(article)){
            return "redirect:/article/" + id;
        }
        Category category= this.categoryRepository.findOne(articleBindingModel.getCategoryId());
        HashSet<Tag>tags = this.findTagsFromString(articleBindingModel.getTagString());
        article.setTags(tags);
        article.setCategory(category);
        article.setContent(articleBindingModel.getContent());
        article.setTitle(articleBindingModel.getTitle());
        this.articleRepository.saveAndFlush(article);
        return "redirect:/article"+article.getId();

    }


    @GetMapping("/article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete (Model model , @PathVariable Integer id){
        if (!this.articleRepository.exists(id)){
            return "redirect:/";
        }
        Article article = this.articleRepository.findOne(id);
        if(!isUserAuthorOrAdmin(article)){
            return "redirect:/article/" + id;
        }
        model.addAttribute("article", article);
        model.addAttribute("view" , "article/delete");

        return "base-layout";
    }


    @PostMapping("/article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProcess(@PathVariable Integer id){

        if (!this.articleRepository.exists(id)) {

            return "redirect:/  ";
        }

            Article article = this.articleRepository.findOne(id);
        if (!isUserAuthorOrAdmin(article)){
            return "redirect:/article/" + id;
        }

            this.articleRepository.delete(article);

            return "redirect:/";
    }
    private boolean isUserAuthorOrAdmin(Article article){
        UserDetails user = (UserDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User userEntity = this.userRepository.findByUserName(user.getUsername());

        return userEntity.isAdmin() || userEntity.isAuthor(article);
    }
    @Autowired
    private TagRepository tagRepository;

    private HashSet<Tag>findTagsFromString(String tagString){
        HashSet<Tag>tags = new HashSet<>();
        String[]tagNames  = tagString.split(",\\s*");

        for (String tagName : tagNames){
            Tag currentTag = this.tagRepository.findByName(tagName);
            if (currentTag == null){
                currentTag = new Tag(tagName);
                this.tagRepository.saveAndFlush(currentTag);
            }
            tags.add(currentTag);
        }
        return tags;
    }

    private static String encodeFileToBase64Binary(File file) {
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return encodedfile;
    }
}
