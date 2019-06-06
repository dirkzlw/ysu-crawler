package com.zlw.ysu.crawler.service.impl;

import com.zlw.ysu.crawler.pojo.User;
import com.zlw.ysu.crawler.repository.UserRepository;
import com.zlw.ysu.crawler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

/**
 * @author Ranger
 * @create 2019-05-29 15:53
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 保存用户
     * @param user
     */
    @Override
    public void saveUser(User user) {
        //根据学号和密码确定用户是否存在
        User exitUser = userRepository.findByUsername(user.getUsername());
        if (exitUser == null) {
            userRepository.save(user);
        }
    }
}
