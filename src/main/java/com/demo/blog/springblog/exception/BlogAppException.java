package com.demo.blog.springblog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class BlogAppException extends RuntimeException{

    private static final Long serialversionUID = 1L;

    private HttpStatus estado;
    private String mensaje;

}
