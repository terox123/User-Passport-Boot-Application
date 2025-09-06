package com.UserPassportBoot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserPassportBootApplicationTests {

	@Test
	void contextLoads() {

	}

	@Test
	void testStartBootApp(){
		UserPassportBootApplication.main(new String[]{});
		assertThat(true).isTrue();
	}



}
