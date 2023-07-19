package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;

/**
 * ユーザー情報 Repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findByAddressStartingWith(String address);

	List<User> findByAddressEndingWith(String address);

	List<User> findByAddressContaining(String address);
}