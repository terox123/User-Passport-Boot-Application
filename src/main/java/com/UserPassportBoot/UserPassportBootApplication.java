package com.UserPassportBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class UserPassportBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserPassportBootApplication.class, args);
	}

}
