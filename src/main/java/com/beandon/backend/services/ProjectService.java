package com.beandon.backend.services;

import com.beandon.backend.pojo.project.PartialProjectData;
import com.beandon.backend.pojo.project.ProjectData;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    private final JdbcTemplate jdbcTemplate;

    public ProjectService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<ProjectData> getAllProjects() {
        return jdbcTemplate.query(
                "SELECT * FROM projects ORDER BY progress;",
                new BeanPropertyRowMapper<>(ProjectData.class));
    }

    public void createProject(PartialProjectData data) {
        jdbcTemplate.update(
                "INSERT INTO projects (id, name, progress, state, link) VALUES (?,?,?,?,?);",
                UUID.randomUUID().toString(),
                data.getName(),
                data.getProgress(),
                data.getState(),
                data.getLink()
        );
    }

    public void updateProject(PartialProjectData data, String id) {
        jdbcTemplate.update(
                "UPDATE projects SET name = ?, progress = ?, state = ?, link = ? WHERE id = ? LIMIT 1;",
                data.getName(),
                data.getProgress(),
                data.getState(),
                data.getLink(),
                id
        );
    }

    public void deleteProject(String id) {
        jdbcTemplate.update(
                "DELETE FROM projects WHERE id = ? LIMIT 1;",
                id
        );
    }
}
