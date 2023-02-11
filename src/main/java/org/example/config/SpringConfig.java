package org.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.model.Cat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "org.example")
@PropertySource("classpath:database.properties")
public class SpringConfig {

    @Autowired
    private Environment environment;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(environment.getProperty("db.driver"));
        config.setJdbcUrl(environment.getProperty("db.host"));
        return new HikariDataSource(config);
    }

    @Bean("rowmapper")
    public RowMapper<Cat> catRowMapper() {
        return ((rs, rowNum) -> new Cat(
                rs.getLong("Id"),
                rs.getString("Name"),
                rs.getInt("Weight"),
                rs.getBoolean("isAngry")
        ));
    }

}
