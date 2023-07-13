package com.javaspringboot.Project2;

import com.javaspringboot.Project2.enumm.ERole;
import com.javaspringboot.Project2.model.Role;
import com.javaspringboot.Project2.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Project2Application implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;
	public static void main(String[] args) {
		SpringApplication.run(Project2Application.class, args);
	}

	// Hàm sẽ được gọi sau khi project được khởi chạy
	@Override
	public void run(String... args) throws Exception {
		//roleRepository.save(new Role(ERole.ROLE_ADMIN));
		//roleRepository.save(new Role(ERole.ROLE_USER));
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods("*").allowedOriginPatterns("http://localhost:3000");
			}
		};
	}

}
