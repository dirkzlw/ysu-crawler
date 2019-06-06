package com.zlw.ysu.crawler.repository;

import com.zlw.ysu.crawler.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Ranger
 * @create 2019-05-29 15:48
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
