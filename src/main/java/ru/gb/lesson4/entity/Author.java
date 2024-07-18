package ru.gb.lesson4.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "author")
public class Author { // final нельзя
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

//    public Author(){
//    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
