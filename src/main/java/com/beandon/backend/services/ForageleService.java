package com.beandon.backend.services;


import com.beandon.backend.pojo.CompletePlantData;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;


@Service
public class ForageleService {
    private final JdbcTemplate jdbcTemplate;

    public ForageleService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<CompletePlantData> getAllPlants() {
        return jdbcTemplate.query(
                "SELECT * FROM plants " +
                        "INNER JOIN plants_names ON plants.id = plants_names.id " +
                        "INNER JOIN selected_plants on plants.id = selected_plants.id;",
                new BeanPropertyRowMapper<>(CompletePlantData.class));
    }

    public CompletePlantData getPlant(String id) {
        return jdbcTemplate.queryForObject(
                String.format("SELECT * FROM plants " +
                                "INNER JOIN plants_names ON plants.id = plants_names.id " +
                                "INNER JOIN selected_plants ON plants.id = selected_plants.id " +
                                "where plants.id='%s';",
                        id),
                new BeanPropertyRowMapper<>(CompletePlantData.class));
    }

    @Transactional
    public void writePlant(CompletePlantData plant) {
        jdbcTemplate.update(
                "INSERT INTO plants (id,name,region,edibility,mostNotableFeature,imageUrl) VALUES (?,?,?,?,?,?);",
                plant.getId(),
                plant.getEnglish(),
                plant.getRegion(),
                plant.getEdibility(),
                plant.getMostNotableFeature(),
                plant.getImageUrl()
        );
        jdbcTemplate.update(
                "INSERT INTO plants_names (id,english,latin) VALUES (?,?,?);",
                plant.getId(),
                plant.getEnglish(),
                plant.getLatin()
        );
        jdbcTemplate.update(
                "INSERT INTO selected_plants (id,start,end) VALUES (?,?,?);",
                plant.getId(),
                plant.getStart(),
                plant.getEnd()
        );
    }

    // Utility function for ease of use
    public void deleteAllPlants() {
        jdbcTemplate.update("DELETE FROM plants;");
        jdbcTemplate.update("DELETE FROM plants_names;");
        jdbcTemplate.update("DELETE FROM selected_plants;");
    }
}
