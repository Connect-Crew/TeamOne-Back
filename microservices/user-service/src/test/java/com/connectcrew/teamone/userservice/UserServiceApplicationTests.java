package com.connectcrew.teamone.userservice;

import com.connectcrew.teamone.userservice.repository.FavoriteRepository;
import com.connectcrew.teamone.userservice.repository.PartRepository;
import com.connectcrew.teamone.userservice.repository.ProfileRepository;
import com.connectcrew.teamone.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class UserServiceApplicationTests {

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private ProfileRepository profileRepository;

	@MockBean
	private FavoriteRepository favoriteRepository;

	@MockBean
	private PartRepository partRepository;

	@Test
	void contextLoads() {
	}

}
