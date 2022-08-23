package com.atguigu.jpa.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@ToString(exclude = {"author"})
@Entity   //表示这个类是一个实体类
@Table(name = "article")    //对应的数据库中表名称
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    @Column(name = "id", nullable = false)
    private Long id;


    @Column(nullable = false, length = 50) // 映射为字段，值不能为空
    private String title;

    @Lob  // 大对象，映射 MySQL 的 Long Text 类型
    @Basic(fetch = FetchType.LAZY) // 懒加载
    @Column(nullable = false) // 映射为字段，值不能为空
    private String content;//文章全文内容

    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)//可选属性optional=false,表示author不能为空。删除文章，不影响用户
    @JoinColumn(name="author_id")//设置在article表中的关联字段(外键)
    private Author author;//所属作者
}