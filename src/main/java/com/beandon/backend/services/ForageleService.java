package com.beandon.backend.services;


import com.beandon.backend.pojo.Plant;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;


@Service
public class ForageleService {

    private final JdbcTemplate jdbcTemplate;

    public ForageleService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Plant> getAllPlants() {
        return jdbcTemplate.query("SELECT * from plants;",
                new BeanPropertyRowMapper<>(Plant.class));
    }

    public Plant getPlant(String id) {
        return jdbcTemplate.queryForObject(String.format("SELECT * from plants where id='%s';", id),
                new BeanPropertyRowMapper<>(Plant.class));
    }

    public void writePlant(Plant plant) {
        jdbcTemplate.update(
                "INSERT INTO plants (id,name,region,edibility,mostNotableFeature,imageUrl) VALUES (?,?,?,?,?,?);",
                plant.getId(),
                plant.getName(),
                plant.getRegion(),
                plant.getEdibility(),
                plant.getMostNotableFeature(),
                plant.getImageUrl()
        );
    }

    // Utility function for ease of use
    public void deleteAllPlants() {
        jdbcTemplate.update("DELETE FROM plants;");
    }
}
