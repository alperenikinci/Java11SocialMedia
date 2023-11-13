package com.bilgeadam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*
    7071'de ayağa kalkan bir user-service modulü oluşturalım.

    Postgre bağlantılarını da gerçekleştirelim.
        -> auth-service icin : java11AuthDB
        -> user-service icin : java11UserDB
 */
@SpringBootApplication
public class AuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class);
    }
}