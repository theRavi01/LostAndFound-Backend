package com.lostandfound.ImgCloud;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class ProjectConfig {

	@Bean
	public Cloudinary getCloudinary() {
		Map config = new HashMap();
		
		config.put("cloud_name","dmgwwxfbi");
		config.put("api_key","999182817265942");
		config.put("api_secret","uRIcbrWWGoC5cQGmdZhbGh9QKlY");
		config.put("secure",true);
		return new Cloudinary(config);
	}
}
