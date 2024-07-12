package com.lostandfound.Entity;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table
public class Users implements UserDetails{
	@Id
	@jakarta.persistence.Column(name = "id")
	@jakarta.persistence.GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(strategy = "native", name = "native")
	private long id;

	@Column(name ="first_name")
	private String firstName;
	
	@Column(name ="last_name")
	private String lastName;
	
	@Column(name="email",columnDefinition="varchar(320)",nullable=false)
	private String email;
	
	@Column(name="password")
	private String password;
	
	@Column( name = "phone_number", columnDefinition = "varchar(20)")
	private String phone;
	
	@Column(name ="otp")
	private String otp;
	
//	@Column(name="profile_image_url",columnDefinition="varchar(2000)")
//	private String profileImgUrl;
	
	@Column(name = "dob", length = 19)
	private Date dob;
	
	@Column(name = "gender")
	private String gender;
	
	@Column(name = "role")
	private String role;
	
	private boolean enable;
	
	public boolean isEnable() {
		return enable;
	}
	
//    @OneToMany(mappedBy = "user")
//    private List<Item> items;
	
	 @Column(name = "picture_url")
		private String pictureUrl;

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
		
			return null;
		}

		@Override
		public String getUsername() {
		
			return this.email;
		}

		@Override
		public boolean isAccountNonExpired() {
			
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			
			return true;
		}

		@Override
		public boolean isEnabled() {
		
			return isEnable();
		}

}
