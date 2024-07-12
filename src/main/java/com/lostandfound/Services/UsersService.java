package com.lostandfound.Services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lostandfound.Entity.Item;
import com.lostandfound.Entity.Users;
import com.lostandfound.Repo.ItemsRepo;
import com.lostandfound.Repo.UsersRepo;
import com.lostandfound.helper.ResponseEntityObject;

import jakarta.mail.internet.MimeMessage;


@Service
public class UsersService {

	@Autowired
	private UsersRepo userRepo;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
  public ResponseEntityObject<Optional<Users>> getUser(String email){
	   Optional<Users> users= userRepo.findByEmail(email);
	   return  new ResponseEntityObject<Optional<Users>>(true, "Succesfully Registered",users);
	   
  }
  
  public ResponseEntityObject<Users> createUser(Users user, String url) {
	    if (userRepo.existsByEmail(user.getEmail())) {
	        return new ResponseEntityObject<Users>(false, "User Already Exist...");
	    }
	    user.setOtp(UUID.randomUUID().toString());
	    user.setPassword(passwordEncoder.encode(user.getPassword()));
	    
	    try {
	        sendEmail(user, url);
	        userRepo.save(user);
	        return new ResponseEntityObject<Users>(true, "Successfully Registered", user);
	    } catch (Exception e) {
	        return new ResponseEntityObject<Users>(false, "Failed to send verification email. User not saved.");
	    }
	}
  
	@Autowired
	private ItemsRepo itemsRepo;
	
	public ResponseEntityObject<Item> createItems(Item items, String username) {
	    Optional<Users> user1 = userRepo.findByEmail(username);
	    if (user1.isPresent()) {
	        Users user = user1.get();
	        items.setUser(user); // Assuming setUser takes a User object
	        itemsRepo.save(items);
	        return new ResponseEntityObject<Item>(true, "Successfully Registered", items);
	    } else {
	        return new ResponseEntityObject<Item>(false,"User not found");
	    }
	}


	  
	  public List<Item> getItems(){
		  return itemsRepo.findAll();
	  }
	  
	  public ResponseEntityObject<Users> updateUser(Users user) {
		   userRepo.save(user);
		   
			return new ResponseEntityObject<Users>(true, "Succesfully updated",user);
	  }
	  
	  public ResponseEntityObject<Optional<Item>> getItem(Long id){
		   Optional<Item> item= itemsRepo.findById(id);
		   return  new ResponseEntityObject<Optional<Item>>(true, "Succesfully ",item);
		   
	  }
	  
	  public void sendEmail(Users user, String url) {
          String from ="lostandfound.inmantec@gmail.com";
          String to = user.getEmail();
          String subject = "Account Verification";
          String content = "<html>" +
                  "<body>" +
                  "<p>Dear " + user.getFirstName() + " " + user.getLastName() + ",</p>" +
                  "<p>Please click the link below to verify your account:</p>" +
                  "<h3><a href=\"" + url + "/verify?code=" + user.getOtp() + "\" target=\"_self\">Verify</a></h3>" +
                  "<p>Thank you,<br>lostandfound.inmantec@gmail.com</p>" +
                  "</body>" +
                  "</html>";
          try {
        	 MimeMessage message = mailSender.createMimeMessage();
        	 MimeMessageHelper helper = new MimeMessageHelper(message);
        	 
        	 helper.setFrom(from,"Lost And Found Inmantec");
        	 helper.setTo(to);
        	 helper.setSubject(subject);
        	 
        	 content =content.replace("[[name]]",user.getFirstName()+" "+user.getLastName());
        	 String siteUrl = url + "/verify?code=" + user.getOtp();
        	 content = content.replace("[[URL]]",siteUrl);
        	 helper.setText(content,true);
        	 mailSender.send(message);
          }
          catch (Exception e) {
			
		}
	}
	  
	  public boolean verifyAccount(String verificationCode) {
			Users user = userRepo.findByOtp(verificationCode);
			if(user == null) {
				return false;
			}
			else {
			user.setEnable(true);
			user.setOtp(null);
			userRepo.save(user);
			return true;
			}
		}
  
	  public List<Item> findItemsByUserID(Long id){
		  return itemsRepo.findByUserId(id);
	  }
}
