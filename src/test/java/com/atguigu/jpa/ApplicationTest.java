package com.atguigu.jpa;

import com.atguigu.jpa.dao.AccountRepository;
import com.atguigu.jpa.dao.ArticleRepository;
import com.atguigu.jpa.dao.AuthorRepository;
import com.atguigu.jpa.dao.UserRepository;
import com.atguigu.jpa.domain.*;
import com.atguigu.jpa.dto.UserOnlyIdAndUsername;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
public class ApplicationTest {

    @Resource
    UserRepository userRepository;

    /**
     * ID查询
     */
    @Test
    @Transactional
    void content(){
        User user = userRepository.getById(1);
        System.out.println(user);
        System.out.println("TEST SUCCESS");
    }

    /**
     * 新增
     */
    @Test
    @Rollback(false)
    void save(){
        User entity = new User();
        entity.setUsername("t1");
        User save = userRepository.save(entity);
        System.out.println(save);
    }

    @Test
    @Rollback(false)
    void delete(){
        userRepository.deleteById(6);
    }

    @Test
    @Rollback(false)
    void page(){
        userRepository.findAll(PageRequest.of(0,1)).forEach(System.out::println);
    }

    @Test
    void testFind(){
        userRepository.findAllByUsername("zhansgan").forEach(System.out::println);
        userRepository.findAllByUsernameAndPassword("zhangsan", "1234").forEach(System.out::println);
        userRepository.findAllByUsernameLike("z%").forEach(System.out::println);
    }

    @Test
    void testSQL(){
        userRepository.findUsers().forEach(System.out::println);
        userRepository.findUsersByUsername().forEach(System.out::println);
        userRepository.findAllUsersFilterOnlyId().forEach(System.out::println);
        userRepository.findAllUsersFilterIdAndUsername().forEach(System.out::println);
        System.out.println("userRepository.findAllUserCount() = " + userRepository.findAllUserCount());


    }

    @Test
    void testUpdate(){
        Integer result = userRepository.updateByUsernameAndPassword("zhangsan222", "123", 1);
        System.out.println(result);
    }



    @Test
    void testUpdateSQL(){
        Integer result = userRepository.updateByUsernameAndPasswordSQL("zhangsan222333", "12sadf3", 1);
        System.out.println(result);
    }

    @Autowired
    AccountRepository accountRepository;
    @Test
    @Transactional
    @Rollback(false)
    void oneToOne(){
        Account account = new Account();
        account.setUsername("ti1");
        account.setPassword("123");
        AccountDetail accountDetail = new AccountDetail();
        accountDetail.setEmail("asd@qwe");
        accountDetail.setAddress("beijing");
        account.setAccountDetail(accountDetail);
        Account save = accountRepository.save(account);
        System.out.println(save+"->"+save.getAccountDetail());
    }

    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Test
    public void toMany() {
        Author author=new Author();
        author.setName("it2");
        Author author1 = authorRepository.save(author);


        Article article1=new Article();
        article1.setTitle("1");
        article1.setContent("123");
        article1.setAuthor(author1);
        articleRepository.save(article1);


        Article article2=new Article();
        article2.setTitle("2");
        article2.setContent("22222");
        article2.setAuthor(author1);
        articleRepository.save(article2);


    }

    @Transactional
    @Test
    public void testFind15() {
        Optional<Author> byId = authorRepository.findById(1L);
        if (byId.isPresent()){
            Author author = byId.get();
            List<Article> articleList = author.getArticleList();
            System.out.println(articleList);
        }
    }






    }