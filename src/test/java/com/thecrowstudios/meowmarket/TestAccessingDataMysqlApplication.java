package com.thecrowstudios.meowmarket;

import org.springframework.boot.SpringApplication;

public class TestAccessingDataMysqlApplication {

	public static void main(String[] args) {
		SpringApplication.from(MeowmarketApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
