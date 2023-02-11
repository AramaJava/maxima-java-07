package org.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.model.Cat;
import org.example.repository.AdvancedCatRepository;
import org.example.repository.CatRepository;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;


import static junit.framework.TestCase.*;

public class CatRepositoryTest {
    private CatRepository repo;

    @Before
    public void initDBconnection() throws IOException {

        String dbPropsPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("database.properties")).getPath();
        Properties dbProps = new Properties();
        dbProps.load(new FileInputStream(dbPropsPath));

        HikariConfig config = new HikariConfig();
        config.setDriverClassName(dbProps.getProperty("db.driver"));
        config.setJdbcUrl(dbProps.getProperty("db.host"));
        HikariDataSource ds = new HikariDataSource(config);

        repo = new AdvancedCatRepository(ds);
    }

    @Test
    public void shouldCRUDWorks() throws SQLException {

        Cat cat1 = new Cat(1L, "Мурзик", 10, true);
        Cat cat2 = new Cat(2L, "Рамзес", 2, false);
        Cat cat3 = new Cat(3L, "Барсик", 5, true);
        Cat cat4 = new Cat(4L, "Мурка", 4, false);
        Cat cat5 = new Cat(5L, "Карл", 7, true);

        repo.create(cat1);
        repo.create(cat2);
        repo.create(cat3);
        repo.create(cat4);
        repo.create(cat5);

        List<Cat> cats = repo.findAll();
        assertEquals(5, cats.size());

        Cat testCat = repo.read(3L);
        assertEquals("Барсик", testCat.getName());
        assertEquals(5, testCat.getWeight());
        assertTrue(testCat.isAngry());

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
