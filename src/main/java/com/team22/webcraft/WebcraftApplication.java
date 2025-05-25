package com.team22.webcraft;

import com.team22.webcraft.Domain.AccessType;
import com.team22.webcraft.Domain.MapData;
import com.team22.webcraft.Repository.MapDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WebcraftApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebcraftApplication.class, args);
	}

}
