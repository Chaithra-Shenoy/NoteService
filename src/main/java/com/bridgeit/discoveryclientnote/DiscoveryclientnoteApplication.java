package com.bridgeit.discoveryclientnote;

import java.util.Collections;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


@EnableEurekaClient
@SpringBootApplication
@RibbonClient(name="NoteService")
@EnableFeignClients
public class DiscoveryclientnoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscoveryclientnoteApplication.class, args);
	}
	
}
