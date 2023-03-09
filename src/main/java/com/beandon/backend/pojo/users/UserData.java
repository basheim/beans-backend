package com.beandon.backend.pojo.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserData {
    private String id;
    private String username;
    private String password;
    private String role;
}
