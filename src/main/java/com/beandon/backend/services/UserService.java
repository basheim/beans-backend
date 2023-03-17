package com.beandon.backend.services;

import com.beandon.backend.pojo.users.UserData;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final JdbcTemplate jdbcTemplate;

    public UserService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<UserData> getUser(String username) {
        return jdbcTemplate.query(
                "SELECT * FROM users WHERE username = ? LIMIT 1;",
                new BeanPropertyRowMapper<>(UserData.class),
                username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserData> maybeUserData = getUser(username);
        if (maybeUserData.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        UserData userData = maybeUserData.get(0);
        return User.withUsername(userData.getUsername())
                .password(userData.getPassword())
                .roles(userData.getRole())
                .build();
    }
}
