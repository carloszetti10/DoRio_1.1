package com.projeto.domRio1.doRio.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;



@Entity
@Data
public class Account {

    @Id
    private String loginId;
    private String name;
    private Role role;
    private String password;

    public enum Role {
        Admin, Sale, Purchase
    }
}
