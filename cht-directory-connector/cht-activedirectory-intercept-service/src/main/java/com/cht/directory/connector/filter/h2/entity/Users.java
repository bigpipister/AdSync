package com.cht.directory.connector.filter.h2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        })
})
public class Users {
    @Id
    @NotBlank
    @Size(min=3, max = 50)
    private String username;

    @NotBlank
    @Size(max = 250)
    private String password;

    @NotBlank
    @Size(max = 250)
    private String hashcode;

    private java.sql.Timestamp activetime;

    private Integer ecounter;

    private Boolean status;

    public Users() {}

    public Users(String username, String password, String hashcode,
                 java.sql.Timestamp activetime, Integer ecounter, Boolean status) {
        this.username = username;
        this.password = password;
        this.hashcode = hashcode;
        this.activetime = activetime;
        this.ecounter = ecounter;
        this.status = status;
    }
}