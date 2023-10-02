package com.reservation.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Housing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private Image previewImage;

    @Column(nullable = false)
    private String coordinates;

    @Column(nullable = false, columnDefinition = "DECIMAL(8,2)")
    private BigDecimal pricePerNight;

    @Column(nullable = false)
    private Integer people;

    @Column(columnDefinition = "DECIMAL(3,1)")
    private BigDecimal rating;

    @Column(nullable = false)
    private Time checkIn;

    @Column(nullable = false)
    private Time checkOut;

    @Column(nullable = false)
    private Integer minRentalAge;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer rooms;

    @Column(nullable = false)
    private Integer m2;

    @Column(nullable = false)
    private Integer minNights;

    @Column(nullable = false)
    private Boolean published;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Location location;

    @JoinColumn(nullable = false)
    @ManyToOne
    private User owner;

    @OneToMany(mappedBy = "housing")
    private List<Review> reviews;

    @OneToMany(mappedBy = "housing")
    private List<Image> images;

    @OneToMany(mappedBy = "housing")
    private List<Booking> bookings;
}