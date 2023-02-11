package org.example.repository;

import org.example.model.Cat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SpringCatRepository implements CatRepository {

    private static String READ_CAT = "SELECT * FROM cats WHERE id=?;";
    private static String INSERT_CAT = "INSERT INTO cats (id, name, weight, isAngry) VALUES (?,?,?,?);";
    private static String UPDATE_CAT = "UPDATE cats SET name=?, weight=?, isAngry=? WHERE id=?;";
    private static String DELETE_CAT = "DELETE FROM cats WHERE id=?;";
    private static String CREATE_CATS = "CREATE TABLE cats (id BIGINT PRIMARY KEY, name VARCHAR(45) NOT NULL, weight SMALLINT NOT NULL, isAngry BIT);";
    private static String FINDALL_CATS = "SELECT * FROM cats;";


    @Autowired
    private DataSource dataSource;
    @Autowired
    private RowMapper<Cat> catRowMapper;
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute(CREATE_CATS);

        create(new Cat(1L, "Мурзик", 10, true));
        create(new Cat(2L, "Рамзес", 2, false));
        create(new Cat(3L, "Барсик", 5, true));
        create(new Cat(4L, "Мурка", 4, false));
        create(new Cat(5L, "Карл", 7, true));
    }

    @Override
    public boolean create(Cat element) {
        return jdbcTemplate.update(INSERT_CAT, element.getId(), element.getName(), element.getWeight(), element.isAngry()) > 0;
    }

    @Override
    public Cat read(Long id) {
        try {
            return jdbcTemplate.query(READ_CAT, new BeanPropertyRowMapper<>(Cat.class), id).get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public int update(Long id, Cat element) {
        return jdbcTemplate.update(UPDATE_CAT, element.getName(), element.getWeight(), element.isAngry(), id);
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_CAT, id);
    }

    @Override
    public List<Cat> findAll() {
        return new ArrayList<>(jdbcTemplate.query(FINDALL_CATS, catRowMapper));
    }
}
