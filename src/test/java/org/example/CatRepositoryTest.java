package org.example;

import org.example.config.SpringConfig;
import org.example.model.Cat;

import org.example.repository.SpringCatRepository;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import java.sql.SQLException;
import java.util.List;



import static junit.framework.TestCase.*;

public class CatRepositoryTest {
    private SpringCatRepository repo;

    @Test
    public void shouldCRUDWorks() throws SQLException {

        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        repo = context.getBean(SpringCatRepository.class);

        List<Cat> cats = repo.findAll();
        assertEquals(5, cats.size());

        repo.findAll().forEach(System.out::println);

        Cat testCat = repo.read(3L);
        assertEquals("Барсик", testCat.getName());
        assertEquals(5, testCat.getWeight());
        assertFalse(testCat.isAngry());

        Cat newCat1 = new Cat(5L, "Карл 3", 7, false);
        Cat newCat2 = new Cat(2L, "Рамзес", 3, true);

        repo.update(newCat1.getId(), newCat1);
        repo.update(newCat2.getId(), newCat2);

        testCat = repo.read(5L);
        assertEquals("Карл 3", testCat.getName());
        assertEquals(7, testCat.getWeight());
        assertFalse(testCat.isAngry());

        repo.delete(1L);

        testCat = repo.read(1L);
        assertNull(testCat);

        repo.delete(4L);

        cats = repo.findAll();
        assertEquals(3, cats.size());

    }
}
