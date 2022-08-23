package com.atguigu.jpa.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity   //表示这个类是一个实体类
@Table(name = "author")    //对应的数据库中表名称
public class Author {
    @Column(name = "id")
    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    private Long id; //id


    @Column(nullable = false, length = 20,name = "name")
    private String name;//姓名

    @OneToMany(mappedBy = "author",cascade=CascadeType.ALL,fetch= FetchType.EAGER)
    //级联保存、更新、删除、刷新;延迟加载。当删除用户，会级联删除该用户的所有文章
    //拥有mappedBy注解的实体类为关系被维护端
    //mappedBy="author"中的author是Article中的author属性
    private List<Article> articleList;//文章列表
}