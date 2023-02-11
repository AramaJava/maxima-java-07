package org.example;

import org.example.config.SpringConfig;
import org.example.repository.SpringCatRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Starter {
    public static void main(String[] args) {
        System.out.println("Hello, Spring JDBC");

        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        SpringCatRepository repo = context.getBean(SpringCatRepository.class);



        repo.findAll().forEach(System.out::println);

        System.out.println(repo.read(2L));
    }
}
