package softuniBlog.controller;

import com.sun.org.apache.regexp.internal.RE;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softuniBlog.entity.Article;
import softuniBlog.entity.Category;
import softuniBlog.repository.ArticleRepository;
import softuniBlog.repository.CategoryRepository;

import java.util.Set;

import javax.jws.WebParam;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ArticleRepository articleRepository;


    @GetMapping("/")
    public String index(Model model) {
        List<Category> categories = this.categoryRepository.findAll();

        model.addAttribute("view", "home/index");
        model.addAttribute("categories", categories);

        return "base-layout";
    }

    @RequestMapping("/error/403")
    public String accessDenied(Model model) {
        model.addAttribute("view", "error/403");

        return "base-layout";
    }

    @GetMapping("/category/{id}")
    public String listArticles(Model model,
                               @PathVariable Integer id,
                               @RequestParam(value = "page", defaultValue = "0") String p,
                               @RequestParam(value = "size", defaultValue = "5") String size
                               ) {

        PageRequest pageable = new PageRequest(Integer.parseInt(p), 2);

        if (!this.categoryRepository.exists(id)) {
            return "redirect:/";
        }
        Category category = this.categoryRepository.findOne(id);
        Set<Article> articles = category.getArticles();

        Page<Article> articlePage = this.articleRepository.findAll(pageable);
        PageWrapper<Article> page = new PageWrapper<Article>(articlePage, "/category/" + id);
        model.addAttribute("articles", page.getContent());
        model.addAttribute("page", page);
        //return "products";

        //model.addAttribute("articles" , articles);
        model.addAttribute("category", category);
        model.addAttribute("view", "home/list-articles");
        return "base-layout";
    }

}
