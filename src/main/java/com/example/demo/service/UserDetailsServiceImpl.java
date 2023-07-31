/**
package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findById(Long.parseLong(userid));
        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("User not found with id: " + userid);
        }

        User user = optionalUser.get();

        UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(userid);
        // この行でハッシュ化されたパスワードを設定します。
        builder.password(user.getPassword());
        builder.authorities(new SimpleGrantedAuthority("USER")); // assuming all users have role "USER"
        return builder.build();
    }


}
**/