package com.example.authentication;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // DBにアクセスするリポジトリを作成する。コードは後述する。
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username); // usernameをクレデンシャルとし、DBにusernameで検索する。
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }

        // Userオブジェクトから情報を取り出し、CustomUserDetailsのコンストラクタに渡す
        String password = user.getPassword();
        String userid = String.valueOf(user.getId()); // User ID を文字列に変換します
        // ここでは、全てのユーザーに "ROLE_USER" の権限を付与しています。
        // 実際のアプリケーションでは、ユーザーの権限をデータベースから取得するか、ユーザーの種類によって異なる権限を付与する必要があるかもしれません。
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        CustomUserDetails customUserDetails = new CustomUserDetails(userid, password, authorities);
        return customUserDetails;

    }

}

