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
                "INSERT INTO plants (id,name,fact1,fact2,fact3,fact4,fact5,image) VALUES (?,?,?,?,?,?,?,?)",
                plant.getId(),
                plant.getName(),
                plant.getFact1(),
                plant.getFact2(),
                plant.getFact3(),
                plant.getFact4(),
                plant.getFact5(),
                plant.getImage()
        );
    }
}
