package com.cht.exchange.h2.entity;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

//@Entity
@Data
//@Table(name = "users", uniqueConstraints = {
//        @UniqueConstraint(columnNames = {
//                "username"
//        })
//})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min=3, max = 50)
    private String username;

    @NotBlank
    @Size(max = 200)
    private String password;

    @NotBlank
    @Size(max = 50)
    private String role;

    public User() {}

    public User(String name, String username, String email, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}