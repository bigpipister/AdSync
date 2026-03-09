package com.cht.exchange.service;

import com.cht.exchange.h2.entity.User;
import com.cht.exchange.h2.repository.UserRepository;
import com.cht.exchange.security.UserPrinciple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

@Service
@Slf4j
public class AuthService implements UserDetailsService {
    //@Resource
    //private UserRepository userRepository;

    /**
     * 從資料庫中找尋用戶
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //log.info("load user by user name");
//        User user = userRepository.findByUsername(username).orElseThrow(
//                () -> new UsernameNotFoundException("User Not Found with -> username or email : " + username));
        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("admin");
        user.setRole("ROLE_ADMIN");
        UserPrinciple userPrinciple = UserPrinciple.build(user);
        //log.info("load user password : " + userPrinciple.getPassword());
        return userPrinciple;
    }
}
