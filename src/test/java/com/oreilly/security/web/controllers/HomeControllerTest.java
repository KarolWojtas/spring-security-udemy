package com.oreilly.security.web.controllers;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.oreilly.security.domain.repositories.AutoUserRepository;
@RunWith(SpringRunner.class)
public class HomeControllerTest {
	private HomeController homeController;
	private MockMvc mockMvc;
	@Mock
	private AutoUserRepository userRepository;
	@Before
	public void setUp() throws Exception {
		homeController = new HomeController(userRepository);
		mockMvc = MockMvcBuilders.standaloneSetup(homeController)
				.setViewResolvers(new InternalResourceViewResolver("/WEB-INF/views/", ".jsp")).build();
	}

	@Test
	public void testGoRegister() throws Exception {
		mockMvc.perform(get("/register"))
			.andExpect(status().isOk())
			.andExpect(view().name("register"));
	}

}
