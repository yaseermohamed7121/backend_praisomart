//package com.praisomart.backend;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//@Configuration
//public class PasswordRunner {
//
//    @Bean
//    public CommandLineRunner generatePassword() {
//        return args -> {
//            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//            String encoded = encoder.encode("123");
//            System.out.println("Encoded password: " + encoded);
//        };
//    }
//}