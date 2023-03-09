package com.beandon.backend.pojo.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartialUser {
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String role;
}
