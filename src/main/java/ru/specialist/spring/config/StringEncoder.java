package ru.specialist.spring.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Scanner;

public class StringEncoder {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        try (Scanner scanner = new Scanner(System.in)){
            System.out.println(encoder.encode(scanner.nextLine()));
        }
    }
}
