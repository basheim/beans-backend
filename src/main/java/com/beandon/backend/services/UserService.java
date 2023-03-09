package com.beandon.backend.services;

import com.beandon.backend.pojo.users.PartialUser;
import com.beandon.backend.pojo.users.UserData;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Service
public class UserService implements UserDetailsService {

    private final Set<String> excludedCharacters = Set.of("\"", "{", "}", ",");
    private final JdbcTemplate jdbcTemplate;
    private final BCryptPasswordEncoder encoder;

    public UserService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.encoder = new BCryptPasswordEncoder();
    }

    public List<UserData> getUser(String username) {
        return jdbcTemplate.query(
                "SELECT * FROM users WHERE username = ? LIMIT 1;",
                new BeanPropertyRowMapper<>(UserData.class),
                username);
    }

    public boolean usernameExists(String username) {
        return getUser(username).size() != 0;
    }

    public void createUser(PartialUser user) {
        if (usernameExists(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        for (String c : excludedCharacters.stream().toList()) {
            if (user.getPassword().contains(c)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password can not contain " + c);
            }
        }
        String hashedPassword = encoder.encode(user.getPassword());
        jdbcTemplate.update(
                "INSERT INTO users (id,username,password,role) VALUES (?,?,?,?);",
                UUID.randomUUID().toString(),
                user.getUsername(),
                "{bcrypt}" + hashedPassword,
                user.getRole()
        );
    }

    public void deleteUser(String userName) {
        if (usernameExists(userName)) {
            jdbcTemplate.update(
                    "DELETE FROM users WHERE username = ? LIMIT 1;",
                    userName
            );
        }
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
