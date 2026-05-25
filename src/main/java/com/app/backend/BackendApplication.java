package com.app.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class BackendApplication {
	public static void main(String[] args) {
		// For debugging: print whether MONGODB_URI or SPRING_DATA_MONGODB_URI are present (do not print secrets)
		String uri = System.getenv("MONGODB_URI");
		String sUri = System.getenv("SPRING_DATA_MONGODB_URI");
		System.out.println("MONGODB_URI present: " + (uri != null && !uri.isBlank()));
		System.out.println("SPRING_DATA_MONGODB_URI present: " + (sUri != null && !sUri.isBlank()));
		if (sUri != null && sUri.length() > 0) {
			System.out.println("SPRING_DATA_MONGODB_URI prefix: " + sUri.substring(0, Math.min(12, sUri.length())) + "...");
		}
		SpringApplication.run(BackendApplication.class, args);
	}
}
// spring boot test configuration