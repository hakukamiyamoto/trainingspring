package com.example.authentication;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
// これを継承することで、UserDetails作成の際に記述量を減らせる
import org.springframework.security.core.userdetails.User;


public class CustomUserDetails extends User {

	// username,password,authoritiesを引数に取るコンストラクタを用意
	public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

}
