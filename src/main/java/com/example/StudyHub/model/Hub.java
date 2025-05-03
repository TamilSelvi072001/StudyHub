package com.example.StudyHub.model;

import com.example.StudyHub.model.City;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "hubs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Hub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hubId;

    @Column(nullable = false)
    private String hubName;

    @Column(nullable = false)
    private String address;

    // Many hubs belong to one city
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    // One hub has many tables
    @OneToMany(mappedBy = "hub", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HubTable> tables;


}