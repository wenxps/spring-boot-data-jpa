## JPA入门

### 配置数据源

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/jpa?serverTimezone=UTC
    username: root
    password: 000823
```

### 导入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
		<groupId>mysql</groupId>
		<artifactId>mysql-connector-java</artifactId>
		<version>8.0.28</version>
</dependency>
```

### 创建实体类

通过注解方式让数据库知道我们的表长什么样

这里就可以知道表的名称是users(当然你可以任意取名),表的创建一般都有主键和自增操作,这里全部通过注解来完成。这里第一次创建表的时候表名会爆红,我们需要给他手动指定数据库,也就是数据源配置时的数据库

```java
@Data
@Entity //这是实体类
@Table(name = "user") //对应哪张表
public class User {

    @Id //这是主键
    @Column(name = "id")//数据库中的id,对应属性中的id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //
    int id;

    @Column(name = "username")//数据库中的username,对应属性中的username
    String username;

    @Column(name = "password")//数据库中的password,对应属性中的password
    String password;

}
```

这里我们还需要设置自动表定义ddl-auto

```yaml
spring:
  jpa:
    show-sql: true #显示sql
    hibernate:
      ddl-auto: create #自动生成表
```

ddl-auto属性用于设置自动表定义，可以实现自动在数据库中为我们创建一个表，表的结构会根据我们定义的实体类决定，它有4种

- create 启动时删数据库中的表，然后创建，退出时不删除数据表
- create-drop 启动时删数据库中的表，然后创建，退出时删除数据表 如果表不存在报错
- update 如果启动时表格式不一致则更新表，原有数据保留
- validate 项目启动表结构进行校验 如果不一致则报错

### 如何访问我们的表

这里我们需要自定义借口继承接口JpaRepository

```java
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
}
```

注意JpaRepository有两个泛型，前者是具体操作的对象实体，也就是对应的表，后者是ID的类型，接口中已经定义了比较常用的数据库操作。编写接口继承即可，我们可以直接注入此接口获得实现类： 这是查操作

```java
@SpringBootTest
class ApplicationTests {

    @Autowired
    UserRepository userRepository;
    
    @Test
    public void testQuery(){
        userRepository.findById(1).ifPresent(System.out::println);
    }
    
}
```

这里需要注意把create策略改成update,因为create策略会删表中数据

这是增操作

```java
    @Test
    public void testAdd(){
        User user=new User();
        //user.setId(2);
        user.setUsername("it");
        user.setPassword("123");

        User saveUser = userRepository.save(user); //新增 返回的实体中带着实体id
        System.out.println(saveUser);
    }
```

在测试方法中会默认回滚，需要添加 `@Rollback(false)`

这是删操作

```java
@Test
    public void testDel(){
        userRepository.deleteById(3);
    }
```

这是分页操作 每页只显示一个数据 ,大家可以先多添加几条记录后再来测试

```java
@Test
    public void testPageable(){
        userRepository.findAll(PageRequest.of(0,2)).forEach(System.out::println);
    }
```

## 方法命名规则查询

顾名思义，方法命名规则查询就是根据方法的名字，就能创建查询。只需要按照Spring Data JPA提供的方法命名规则定义方法的名称，就可以完成查询工作。Spring Data JPA在程序执行的时候会根据方法名称进行解析，并自动生成查询语句进行查询.

按照Spring Data JPA 定义的规则，查询方法以findBy开头，涉及条件查询时，条件的属性用条件关键字连接，要注意的是：条件属性首字母需大写。框架在进行方法名解析时，会先把方法名多余的前缀截取掉，然后对剩下部分进行解析。

```java
//根据username查询
List<User> findAllByUsername(String str);
```

```java
List<User> findByUsernameAndPassword(String username,String password);
List<User> findByUsernameLike(String username);
```

具体的关键字，使用方法和生产成SQL如下表所示

| Keyword           | Sample                                  | JPQL                                                         |
| ----------------- | --------------------------------------- | ------------------------------------------------------------ |
| And               | findByLastnameAndFirstname              | … where x.lastname = ?1 and x.firstname = ?2                 |
| Or                | findByLastnameOrFirstname               | … where x.lastname = ?1 or x.firstname = ?2                  |
| Is,Equals         | findByFirstnameIs,findByFirstnameEquals | … where x.firstname = ?1                                     |
| Between           | findByStartDateBetween                  | … where x.startDate between ?1 and ?2                        |
| LessThan          | findByAgeLessThan                       | … where x.age < ?1                                           |
| LessThanEqual     | findByAgeLessThanEqual                  | … where x.age ⇐ ?1                                           |
| GreaterThan       | findByAgeGreaterThan                    | … where x.age > ?1                                           |
| GreaterThanEqual  | findByAgeGreaterThanEqual               | … where x.age >= ?1                                          |
| After             | findByStartDateAfter                    | … where x.startDate > ?1                                     |
| Before            | findByStartDateBefore                   | … where x.startDate < ?1                                     |
| IsNull            | findByAgeIsNull                         | … where x.age is null                                        |
| IsNotNull,NotNull | findByAge(Is)NotNull                    | … where x.age not null                                       |
| Like              | findByFirstnameLike                     | … where x.firstname like ?1                                  |
| NotLike           | findByFirstnameNotLike                  | … where x.firstname not like ?1                              |
| StartingWith      | findByFirstnameStartingWith             | … where x.firstname like ?1 (parameter bound with appended %) |
| EndingWith        | findByFirstnameEndingWith               | … where x.firstname like ?1 (parameter bound with prepended %) |
| Containing        | findByFirstnameContaining               | … where x.firstname like ?1 (parameter bound wrapped in %)   |
| OrderBy           | findByAgeOrderByLastnameDesc            | … where x.age = ?1 order by x.lastname desc                  |
| Not               | findByLastnameNot                       | … where x.lastname <> ?1                                     |
| In                | findByAgeIn(Collection ages)            | … where x.age in ?1                                          |
| NotIn             | findByAgeNotIn(Collection age)          | … where x.age not in ?1                                      |
| TRUE              | findByActiveTrue()                      | … where x.active = true                                      |
| FALSE             | findByActiveFalse()                     | … where x.active = false                                     |
| IgnoreCase        | findByFirstnameIgnoreCase               | … where UPPER(x.firstame) = UPPER(?1)                        |

## 使用JPQL的方式查询

使用Spring Data JPA提供的查询方法已经可以解决大部分的应用场景，但是对于某些业务来说，我们还需要灵活的构造查询条件，这时就可以使用@Query注解，结合JPQL的语句方式完成查询。

@Query 注解的使用非常简单，只需在方法上面标注该注解，同时提供一个JPQL查询语句即可，注意：

- 大多数情况下将*替换为别名
- 表名改为类名
- 字段名改为属性名
- 搭配注解@Query进行使用

```java
@Query("select 表别名 from 表名(实际为类名) 别名 where 别名.属性='itlils'")
public List<User> findUsers();
```

### 入门

```java
@Query("select u from User u")//从实体类，查询，而不是表
List<User> findAllUser();
```

### 筛选条件

```java
@Query("select u from User u where u.username='it'")//从实体类，查询，而不是表。where不是列名，而是属性名
    List<User> findAllUserByUsername();
```

### 投影结果

真实的业务当中，我们只想要表里某一些列。

```java
@Query("select u.id from User u")//从实体类，查询，而不是表
    List<Integer> findAllUser2();
```

### 聚合查询

```java
@Query("select count(u) from User u")//从实体类，查询，而不是表
    List<Integer> findAllUser4();
```

### 传参

```java
//修改数据 一定加上@Modifying 注解
    @Transactional
    @Modifying
    @Query("update User set username=?1 where id=?2")
    int updateUsernameById2(String username,Integer id);
```

## 使用原生sql

nativeQuery = true

```java
//修改数据 一定加上@Modifying 注解
    @Transactional
    @Modifying
    @Query(value = "update user u set u.username=?1 where u.id=?2",nativeQuery = true)
    int updateUsernameById3(String username,Integer id);
```

其他就和mybatis一样使用了

## 一对一关系

账户和账户明细

```java
@Data
@Entity   //表示这个类是一个实体类
@Table(name = "account")    //对应的数据库中表名称
public class Account {

    @GeneratedValue(strategy = GenerationType.IDENTITY)   //生成策略，这里配置为自增
    @Column(name = "id")    //对应表中id这一列
    @Id     //此属性为主键
    int id;

    @Column(name = "username")   //对应表中username这一列
    String username;

    @Column(name = "password")   //对应表中password这一列
    String password;
    
    //一对一
    @JoinColumn(name = "detail_id")
    @OneToOne//声明为一对一关系
    AccountDetail detail;//对象类型,也可以理解这里写哪个实体类,外键就指向哪个实体类的主键
}
```

```java
@Data
@Entity
@Table(name = "account_details")
public class AccountDetail {
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)//还是设置一个自增主键
    @Id
    int id;

    @Column(name = "address")
    String address;

    @Column(name = "email")
    String email;

    @Column(name = "phone")
    String phone;

    @Column(name = "real_name")
    String realName;
}
```

这里从日志中可以看出hibernate帮我们完成外键的创建

但这还不能完成同时对两张表进行操作 设置懒加载完成想查什么就查什么功能,设置关联级别完成同时操作两张表

```java
@JoinColumn(name = "detail_id")
@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) //设置关联操作为ALL
AccountDetail detail;
```

这里的关联级别也是有多个,一般设置为all就行

- ALL：所有操作都进行关联操作
- PERSIST：插入操作时才进行关联操作
- REMOVE：删除操作时才进行关联操作
- MERGE：修改操作时才进行关联操作

测试：运行测试类任意方法，边创建完成

现在我们就可以同时添加数据和删除了

```java
@Autowired
    AccountRepository accountRepository;

    @Test
    public void testFind11() {
        Account account=new Account();
        account.setUsername("itlils");
        account.setPassword("ydl666");

        AccountDetail accountDetail=new AccountDetail();
        accountDetail.setPhone("13000000000");
        accountDetail.setRealName("itlils");
        accountDetail.setAddress("cn");
        accountDetail.setEmail("123@qq.com");

        account.setDetail(accountDetail);

        Account save = accountRepository.save(account);

        System.out.println("插入之后account id为："+save.getId()+"|||accountDetail id"+save.getDetail().getId());

    }
```

```java
 @Test
    public void testFind12() {
        accountRepository.deleteById(1);
    }
```

两表都没了数据

## 对多关系

```text
@OneToMany:
   	作用：建立一对多的关系映射
    属性：
    	targetEntityClass：指定多的多方的类的字节码
    	mappedBy：指定从表实体类中引用主表对象的名称。
    	cascade：指定要使用的级联操作
    	fetch：指定是否采用延迟加载
    	orphanRemoval：是否使用孤儿删除
```

实体 Author：作者。

实体 Article：文章。

Author 和 Article 是一对多关系(双向)。那么在[JPAopen in new window](https://liuyanzhao.com/tag/jpa/)中，如何表示一对多的双向关联呢？

JPA 使用@OneToMany和@ManyToOne来标识一对多的双向关联。一端(Author)使用@OneToMany,多端(Article)使用@ManyToOne。

在 JPA 规范中，一对多的双向关系由多端(Article)来维护。就是说多端(Article)为关系维护端，负责关系的增删改查。一端(Author)则为关系被维护端，不能维护关系。

一端(Author)使用@OneToMany注释的mappedBy="author"属性表明Author是关系被维护端。

多端(Article)使用@ManyToOne和@JoinColumn来注释属性 author,@ManyToOne表明Article是多端，@JoinColumn设置在article表中的关联字段(外键)。

```java
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

    @OneToMany(mappedBy = "author",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    //级联保存、更新、删除、刷新;延迟加载。当删除用户，会级联删除该用户的所有文章
    //拥有mappedBy注解的实体类为关系被维护端
    //mappedBy="author"中的author是Article中的author属性
    private List<Article> articleList;//文章列表
}
```

```java
@Data
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
```

新增：

```java
@Autowired
    AuthorRepository authorRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Test
    public void testFind13() {
        Author author=new Author();
        author.setName("itlils");
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
```

删除：

```java
 @Test
    public void testFind14() {
        articleRepository.deleteById(2L);
    }
```

查询：

```text
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
```

> 当我们需要及时加载的时候，报 no session，需要将懒加载改成及时加载
>
> 解决方案：
>
> 作者 fetch=FetchType.EAGER

> 出现toString循环依赖导致栈溢出
>
> 解决方案：循环tostring 破坏一方的tostring即可。文章@ToString(exclude = {"author"})

## 多对多 关系

实体 User：用户。

实体 Authority：权限。

用户和权限是多对多的关系。一个用户可以有多个权限，一个权限也可以被很多用户拥有。

JPA 中使用@ManyToMany来注解多对多的关系，由一个关联表来维护。这个关联表的表名默认是：主表名+下划线+从表名。(主表是指关系维护端对应的表,从表指关系被维护端对应的表)。这个关联表只有两个外键字段，分别指向主表ID和从表ID。字段的名称默认为：主表名+下划线+主表中的主键列名，从表名+下划线+从表中的主键列名。

需要注意的：

1、多对多关系中一般不设置级联保存、级联删除、级联更新等操作。

2、可以随意指定一方为关系维护端，在这个例子中，我指定 User 为关系维护端，所以生成的关联表名称为： user_authority，关联表的字段为：user_id 和 authority_id。

3、多对多关系的绑定由关系维护端来完成，即由 User.setAuthorities(authorities) 来绑定多对多的关系。关系被维护端不能绑定关系，即User不能绑定关系。

4、多对多关系的解除由关系维护端来完成，即由Authority.getUser().remove(user)来解除多对多的关系。关系被维护端不能解除关系，即Authority不能解除关系。

5、如果 User 和 Authority 已经绑定了多对多的关系，那么不能直接删除 Authority，需要由 User 解除关系后，才能删除 Authority。但是可以直接删除 User，因为 User 是关系维护端，删除 User 时，会先解除 User 和 Authority 的关系，再删除 Authority。

```java
@Data
@Entity //这是实体类
@Table(name = "users") //对应哪张表
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, length = 20, unique = true)
    private String username; // 用户账号，用户登录时的唯一标识


    @Column(length = 100)
    private String password; // 登录时密码

    @ManyToMany
    @JoinTable(name = "user_authority",joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    //1、关系维护端，负责多对多关系的绑定和解除
    //2、@JoinTable注解的name属性指定关联表的名字，joinColumns指定外键的名字，关联到关系维护端(User)
    //3、inverseJoinColumns指定外键的名字，要关联的关系被维护端(Authority)
    //4、其实可以不使用@JoinTable注解，默认生成的关联表名称为主表表名+下划线+从表表名，
    //即表名为user_authority
    //关联到主表的外键名：主表名+下划线+主表中的主键列名,即user_id
    //关联到从表的外键名：主表中用于关联的属性名+下划线+从表的主键列名,即authority_id
    //主表就是关系维护端对应的表，从表就是关系被维护端对应的表
    private List<Authority> authorityList;

}
```

```java
@Data
@Entity //这是实体类
@Table(name = "authority") //对应哪张表
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name; //权限名

    @ManyToMany(mappedBy = "authorityList")
    private List<Users> userList;
}
```

测试：添加

```java
 @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Test
    public void testFind16() {
        Authority authority = new Authority();
        authority.setId(1);
        authority.setName("ROLE_ADMIN");
        authorityRepository.save(authority);
    }

    @Test
    public void testFind17() {
        Users users = new Users();
        users.setUsername("itlils");
        users.setPassword("123456");
        Authority authority = authorityRepository.findById(1).get();
        List<Authority> authorityList = new ArrayList<>();
        authorityList.add(authority);
        users.setAuthorityList(authorityList);
        usersRepository.save(users);
    }
```

先运行 saveAuthority 添加一条权限记录，

然后运行 saveUser 添加一条用户记录，与此同时，user_authority 表中也自动插入了一条记录

测试 删除

删除用户

```java
@Test
    public void testFind18() {
        usersRepository.deleteById(1L);
    }
```

user 表中删除一条记录，同时 user_authority 能够级联删除一条记录

中间表和users表数据都删除了