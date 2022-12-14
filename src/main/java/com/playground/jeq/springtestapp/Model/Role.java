package com.playground.jeq.springtestapp.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Role {

    @Id @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String description;

    public Role(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public Role() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
