package com.demo.blog.springblog.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthResponseDTO {

    private String token;
    private String typeByToken = "Bearer";

    public JwtAuthResponseDTO(String token) {
        super();
        this.token = token;
    }
}
