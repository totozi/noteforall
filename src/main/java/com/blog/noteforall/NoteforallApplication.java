package com.blog.noteforall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NoteforallApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoteforallApplication.class, args);
		String osName = System.getProperty("os.name");
		System.out.println("Operating System: " + osName);
	}

}
