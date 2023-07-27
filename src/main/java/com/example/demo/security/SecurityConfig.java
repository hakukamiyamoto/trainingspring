package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig { // WebSecurityConfigurerAdapterは継承しない

	// SecurityFilterChainをBean定義する
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		// config(HttpSecurity http)のオーバーライドしたメソッド内でやっていたことと同じ

		// 以下ログインに関する設定。書き方は以前と同様

		http.formLogin(login -> login
				.loginProcessingUrl("/signin").permitAll() //ログインのURL
				.loginPage("/signin").permitAll()
				.usernameParameter("00")
				.passwordParameter("password")
				.failureUrl("/signin?failed")
				.defaultSuccessUrl("/user/list")

		// 以下ログアウトに関する設定。書き方は以前と同様
		).logout(logout -> logout
				.logoutUrl("/signout").permitAll()
				.logoutSuccessUrl("/signin")
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")

		// URLごとの認可設定を下記で行う。書き方は以前と同様
		).authorizeHttpRequests(authz -> authz
				.mvcMatchers("/signin/**", "/usersignin/**").permitAll()
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
