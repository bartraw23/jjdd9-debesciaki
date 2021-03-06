package com.infoshareacademy.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@NamedQueries({
        @NamedQuery(
                name = "Organizer.findAll",
                query = "SELECT o FROM Organizer o"
        ),
        @NamedQuery(
                name = "Organizer.findActiveOrderByEventCount",
                query = "SELECT o FROM Organizer o WHERE (o.eventList.size > 0) ORDER BY o.designation ASC"
        ),
        @NamedQuery(
                name = "Organizer.findByApiId",
                query = "SELECT o FROM Organizer o WHERE o.apiId=:apiID"
        ),
        @NamedQuery(
                name = "Organizer.findByDesignation",
                query = "SELECT o FROM Organizer o WHERE o.designation=:designation"
        ),
        @NamedQuery(
                name = "Organizer.findByEventId",
                query = "SELECT o FROM Organizer o WHERE (SELECT e FROM Event e WHERE e.id=:eventId) MEMBER OF o.eventList"
        )
})
@Entity
@Table(name = "organizer")
public class Organizer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "api_id", unique = true)
    private Long apiId;

    @Column(name = "designation")
    @NotNull
    private String designation;

    @OneToMany(mappedBy = "organizer")
    private List<Event> eventList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }
}
