package com.example.gaehwa;

//@SpringBootApplication
//public class GaehwaApplication {
//
//    public static void main(String[] args) {
//        SpringApplication.run(GaehwaApplication.class, args);
//    }
//
//}


import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GaehwaApplication {
    public static void main(String[] args) {
        // .env 로드
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });

        SpringApplication.run(GaehwaApplication.class, args);
    }
}

