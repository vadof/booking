package com.reservation.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HousingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @OneToOne(mappedBy = "housingDetails")
    private Housing housing;

    @OneToMany
    private List<Image> images;

    @Column(nullable = false)
    private Time checkIn;

    @Column(nullable = false)
    private Time checkOut;

    @Column(nullable = false)
    private Integer minAgeToRent;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer rooms;

    @Column(nullable = false)
    private Integer m2;

    @Column(nullable = false)
    private Integer minNights;

    @JoinColumn(nullable = false)
    @ManyToOne
    private User owner;

    @OneToMany(mappedBy = "housingDetails")
    private List<Review> reviews;

}
