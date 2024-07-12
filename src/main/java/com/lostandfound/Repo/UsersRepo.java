package com.lostandfound.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lostandfound.Entity.Users;

@Repository
public interface UsersRepo extends JpaRepository<Users,Long>{
	 public Optional<Users> findByEmail(String name);
	 public boolean existsByEmail(String email);
	    public Users findByOtp(String code);
}
