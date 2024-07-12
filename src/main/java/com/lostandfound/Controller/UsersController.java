package com.lostandfound.Controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.lostandfound.Entity.Item;
import com.lostandfound.Entity.Users;
import com.lostandfound.ImgCloud.CloudinaryImageServiceImp;
import com.lostandfound.Repo.UsersRepo;
import com.lostandfound.ServiceImp.UsersServiceImp;
import com.lostandfound.Services.UsersService;
import com.lostandfound.helper.JwtHelper;
import com.lostandfound.helper.JwtRequest;
import com.lostandfound.helper.JwtResponse;
import com.lostandfound.helper.ResponseEntityObject;
import com.lostandfound.request.ItemRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@CrossOrigin
@RestController
@RequestMapping("/api")
public class UsersController {
//	spring security 
	
	@Autowired
	UsersServiceImp usersServiceImpl;
	
	 @Autowired
	    private UserDetailsService userDetailsService;

	    @Autowired
	    private AuthenticationManager manager;
	    

	    @Autowired
		private UsersService usersService;
	    
	    @Autowired
	    private JwtHelper helper;
	    
	    @Autowired
	    private UsersRepo userRepo;
	    
	    @Autowired
	    private CloudinaryImageServiceImp cloudinaryImageService;
	    
	    
		

	    
		private Logger logger = LoggerFactory.getLogger(UsersController.class);


	    @PostMapping("/login")
	    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {

	        this.doAuthenticate(request.getEmail(), request.getPassword());


	        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
	        String token = this.helper.generateToken(userDetails);

	        JwtResponse response = JwtResponse.builder()
	                .jwtToken(token)
	                .username(userDetails.getUsername()).build();
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }

	    private void doAuthenticate(String email, String password) {

	        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
	        try {
	            manager.authenticate(authentication);


	        } catch (BadCredentialsException e) {
	            throw new BadCredentialsException(" Invalid Username or Password  !!");
	        }

	    }

	    @ExceptionHandler(BadCredentialsException.class)
	    public String exceptionHandler() {
	        return "Credentials Invalid !!";
	    }
	    
	    @PostMapping("/create-user")
	    public ResponseEntityObject<JwtResponse> createUser(@RequestBody Users users
	    		,HttpServletRequest req) {
	    	String url = req.getRequestURL().toString();
	    	url = url.replace(req.getServletPath(),"/api");
//	    	String url ="http://192.168.109.236/api";
	        ResponseEntityObject<Users> userResponse = usersService.createUser(users,url);
	        
	        if (userResponse.isStatus()) { // Check if the user creation was successful
	            UserDetails userDetails = userDetailsService.loadUserByUsername(users.getEmail());
	            String token = this.helper.generateToken(userDetails);

	            JwtResponse response = JwtResponse.builder()
	                    .jwtToken(token)
	                    .username(userDetails.getUsername()).build();

	            return new ResponseEntityObject<>(true, userResponse.getMessage());
	        } else {
	            return new ResponseEntityObject<>(false, userResponse.getMessage());
	        }
	    }

	    
	    @PostMapping("/update-user")
	    public ResponseEntity<ResponseEntityObject> updateUser(@RequestBody Users users) {
			return new ResponseEntity<ResponseEntityObject>(usersService.updateUser(users),HttpStatus.OK);
	    }

	    @PostMapping("/profile-image")
		public ResponseEntity<Map> uploadImage(@RequestParam("image") MultipartFile file ,@Valid @RequestHeader("Authorization") String token){
		try {
				Users user = new Users(); 
		    	if (token != null) {
		             // Extract the token value
		    		  String username = helper.getUsernameFromToken(token.substring(7));
		             Optional<Users> userDetail = userRepo.findByEmail(username);
			    		user = userDetail.get();
			    		System.out.println(user);
		    	}
		     		Map data = this.cloudinaryImageService.upload(file);
		    		String imageUrl=String.valueOf(data.get("url"));
		    		System.out.println(imageUrl);
		             user.setPictureUrl(imageUrl);
		             userRepo.save(user);
			       return new ResponseEntity<Map> (data , HttpStatus.OK);
			}
			catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<Map> (HttpStatus.BAD_REQUEST);
			}
		}
	// current login users details
	@PostMapping("/loggedInUsers")
	public String getLoggedInUsers(Principal principal) {
		return principal.getName();
	}

	@GetMapping("/user")
	public ResponseEntity<ResponseEntityObject> findByUserName(@RequestParam(value="email")String email) {
	    return new ResponseEntity<ResponseEntityObject>(usersService.getUser(email),HttpStatus.OK);
	}
	

	
	@GetMapping("/items")
	public List<Item> allItems(){
		return usersService.getItems();
	}
	
	
	@GetMapping("/item")
	public ResponseEntity<ResponseEntityObject> findById(@RequestParam(value="id")Long id) {
	    return new ResponseEntity<ResponseEntityObject>(usersService.getItem(id),HttpStatus.OK);
	}
	
	@PostMapping("/create-item")
    public ResponseEntity<ResponseEntityObject> createUser(@ModelAttribute ItemRequest itemRequest, @RequestHeader("Authorization") String token) {
        String username = helper.getUsernameFromToken(token.substring(7)); // Remove "Bearer " prefix
        System.out.println(username);

        Map<String, Object> data = cloudinaryImageService.upload(itemRequest.getImage());
        String imageUrl = String.valueOf(data.get("url"));
        System.out.println(imageUrl);

        Item items = new Item();
        items.setItemName(itemRequest.getItemName());
        items.setPlace(itemRequest.getPlace());
        items.setDate(itemRequest.getDate());
        items.setItemPictureUrl(imageUrl);
        items.setType(itemRequest.getType());
        items.setRewardPrice(itemRequest.getRewardPrice());
        items.setDescription(itemRequest.getDescription());
        items.setCategory(itemRequest.getCategory());

        return new ResponseEntity<>(usersService.createItems(items, username), HttpStatus.CREATED);
    }
	  
	  @GetMapping("/verify")
		public String verifyAccount(@Param("code")String code) {
			boolean f = usersService.verifyAccount(code);
			if(f) {
				return "Sucessfully your account is verfied..";
			}
			else {
				return "May be your verification code is incorrect or already verified";
			}
		}
	  
	  @PostMapping("/userItems")
	  public ResponseEntityObject<List<Item>> findItemsByUserId(@Valid @RequestHeader("Authorization") String token){
	        String username = helper.getUsernameFromToken(token.substring(7)); // Remove "Bearer " prefix
	       Optional<Users> user = userRepo.findByEmail(username);
	       Long id = user.get().getId();
	       return new ResponseEntityObject<List<Item>>(true,"All items of user", usersService.findItemsByUserID(id));
		  
		  
	  }
}
