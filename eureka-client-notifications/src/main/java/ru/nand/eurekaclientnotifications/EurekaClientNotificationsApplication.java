package ru.nand.eurekaclientnotifications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EurekaClientNotificationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaClientNotificationsApplication.class, args);
	}

}
