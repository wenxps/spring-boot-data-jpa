package com.atguigu.jpa.dao;

import com.atguigu.jpa.domain.User;
import com.atguigu.jpa.dto.UserOnlyIdAndUsername;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

    /**
     * 根据用户名查询数据
     * @param username 用户名
     * @return 用户列表
     */
    List<User> findAllByUsername(String username);

    /**
     * 根据用户名和密码查询数据
     * @param username 用户名
     * @param password 密码
     * @return 用户列表
     */
    List<User> findAllByUsernameAndPassword(String username,String password);

    /**
     * 根据用户名进行模糊查询
     * @param usernameLike 用户名模糊查询
     * @return 用户列表
     */
    List<User> findAllByUsernameLike(String usernameLike);

    /**
     * 自定义SQL
     * @return
     */
    @Query("select u from User u")
    List<User> findUsers();

    /**
     * 自定义查询筛选条件
     * @return
     */
    @Query("select u from User u where u.username = 'zhansgan'")
    List<User> findUsersByUsername();

    /**
     * 只查询某一列
     * @return
     */
    @Query("select u.id from User u")
    List<Integer> findAllUsersFilterOnlyId();

    //@Query(value="
    //select new com.test.ycyin.entity.TestView(t1.id,t1.userName,t2.score)
    // from User t1 Left Join Score t2 on t1.id = t2.userId")

    /**
     * 只返回部分属性
     *   1. 定义dto传输对象用来接收数据
     *   2. 定义有参和无参构造
     *   3. SQL
     * @return 数据列表
     */
    @Query("select new com.atguigu.jpa.dto.UserOnlyIdAndUsername(u.id,u.username) from User u")
    List<UserOnlyIdAndUsername> findAllUsersFilterIdAndUsername();

    /**
     * 自定义SQL 总数
     * @return
     */
    @Query(value = "select count(u) from User u")
    Integer findAllUserCount();

    /**
     * 更新数据自定义SQL
     * @param username
     * @param password
     * @param id
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "update User set username=?1 , password = ?2 where id = ?3")
    Integer updateByUsernameAndPassword(String username,String password,Integer id);

    /**
     * 原生SQL 更新数据
     * @param username
     * @param password
     * @param id
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "update user u set u.username = ?1 , u.password = ?2 where u.id = ?3",nativeQuery = true)
    Integer updateByUsernameAndPasswordSQL(String username,String password,Integer id);




}