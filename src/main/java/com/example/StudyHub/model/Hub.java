package com.example.StudyHub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hubs")
@Getter
@Setter
@NoArgsConstructor
public class Hub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hubId;

    @Column(nullable = false)
    private String hubName;

    @Column(nullable = false)
    private String address;

    @Column
    private String cityName;

    // Many hubs belong to one city
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    // One hub has many tables
    @OneToMany(mappedBy = "hub", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HubTable> tables = new ArrayList<>();

    // Custom constructor without the collections to avoid circular references
    public Hub(Long hubId, String hubName, String address, String cityName, City city) {
        this.hubId = hubId;
        this.hubName = hubName;
        this.address = address;
        this.cityName = cityName;
        this.city = city;
    }
}
