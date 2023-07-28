package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {
	
	
	

	// SecurityFilterChainをBean定義する
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		
		// ログインに関する設定。

		http.formLogin(login -> login
				.loginProcessingUrl("/signin").permitAll() //ログインのURL
				.loginPage("/signin").permitAll()
				.usernameParameter("userid")
				.passwordParameter("password")
				.failureUrl("/signin?failed")
				.defaultSuccessUrl("/user/list")

		// ログアウトに関する設定。
		).logout(logout -> logout
				.logoutUrl("/signout").permitAll()
				.logoutSuccessUrl("/signin")
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")

		// URLごとの認可設定を下記で行う。
		).authorizeHttpRequests(authz -> authz
				.mvcMatchers("/login/**", "/user/**", "/rest/**","/signin/**", "/usersignin/**","/signin?failed","/img/**", "/css/**", "/js/**").permitAll()
				.anyRequest().authenticated());

		// Bean登録
		return http.build();
	}

	// PasswordEncoderもBean登録する。これが無いと、PasswordEncoderがありませんというエラーがでる。
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
