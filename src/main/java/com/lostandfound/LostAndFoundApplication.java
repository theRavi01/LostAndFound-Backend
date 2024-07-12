package com.lostandfound;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
public class LostAndFoundApplication {

	public static void main(String[] args) {
		SpringApplication.run(LostAndFoundApplication.class, args);
		System.out.println("Its working :)Mr Ravikant !!");
	}

}
