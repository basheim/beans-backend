package com.beandon.backend.pojo.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Auth {
    @NonNull
    private String username;
    @NonNull
    private String password;
}
