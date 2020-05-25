package com.infoshareacademy.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "place")
public class Place {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "subname")
    private String subname;

    @OneToMany(mappedBy = "place")
    List<Event> eventList;
}