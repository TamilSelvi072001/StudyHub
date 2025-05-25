package com.example.StudyHub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "hub_tables")
@Getter
@Setter
@NoArgsConstructor
public class HubTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tableId;

    @Column(nullable = false)
    private String tableNumber; // Can be string like "T1", "T2" etc.

    // Many tables belong to one hub
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hub_id", nullable = false)
    private Hub hub;

    // One table has many seats
    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Seat> seats;

    // Custom constructor without the collections to avoid circular references
    public HubTable(Long tableId, String tableNumber, Hub hub) {
        this.tableId = tableId;
        this.tableNumber = tableNumber;
        this.hub = hub;
    }
}