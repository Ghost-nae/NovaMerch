package com.Nova.Merch;

import org.springframework.boot.SpringApplication;

public class TestMerchApplication {

	public static void main(String[] args) {
		SpringApplication.from(MerchApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
