package com.beandon.backend.services;


import com.beandon.backend.pojo.foragele.CompletePlantData;
import com.beandon.backend.pojo.foragele.PlantLatestDate;
import com.beandon.backend.pojo.foragele.SelectedPlant;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
                "SELECT * FROM plants " +
                            "INNER JOIN plants_names ON plants.id = plants_names.id " +
                            "INNER JOIN selected_plants ON plants.id = selected_plants.id " +
                            "WHERE plants.id=?;",
                new BeanPropertyRowMapper<>(CompletePlantData.class),
                id);
    }

    @Transactional
    public void writePlant(CompletePlantData plant) {
        jdbcTemplate.update(
                "INSERT INTO plants (id,name,edibility,imageUrl,foundNear,keyFeatures,poisonousLookAlike) VALUES (?,?,?,?,?,?,?);",
                plant.getId(),
                plant.getEnglish(),
                plant.getEdibility(),
                plant.getImageUrl(),
                plant.getFoundNear(),
                plant.getKeyFeatures(),
                plant.getPoisonousLookAlike()
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

    public PlantLatestDate getLatestDate() {
        SelectedPlant plantDates = jdbcTemplate.queryForObject(
                "SELECT selected_plants.end FROM plants " +
                        "INNER JOIN plants_names ON plants.id = plants_names.id " +
                        "INNER JOIN selected_plants on plants.id = selected_plants.id " +
                        "ORDER BY end DESC " +
                        "LIMIT 1;",
                new BeanPropertyRowMapper<>(SelectedPlant.class));
        if (plantDates == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Plant dates are null");
        }
        return PlantLatestDate.builder()
                .latestDate(plantDates.getEnd())
                .build();
    }
}
