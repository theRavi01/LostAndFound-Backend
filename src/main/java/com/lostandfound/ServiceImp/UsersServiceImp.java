package com.lostandfound.ServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lostandfound.Entity.Users;
import com.lostandfound.Repo.UsersRepo;


/***
 * @author Ravikant Pandey
 */


@Service
public class UsersServiceImp implements UserDetailsService{

	@Autowired
	UsersRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// load user form database
		Users users = userRepo.findByEmail(username).orElseThrow(() -> new RuntimeException("user not found !!"));
		
		return users;
	}
}
