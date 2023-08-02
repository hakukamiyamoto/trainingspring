package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.entity.User;
import com.example.demo.repository.UserMapper;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
		User user = userMapper.findById(Long.parseLong(userid));
		if (user == null) {
			throw new UsernameNotFoundException("User not found with id: " + userid);
		}

		UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(userid);
		// この行でハッシュ化されたパスワードを設定します。
		builder.password(user.getPassword());
		builder.authorities(new SimpleGrantedAuthority("USER")); // assuming all users have role "USER"
		return builder.build();
	}

}
