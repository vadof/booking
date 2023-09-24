package com.reservation.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
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

    @JsonIgnore
    @JoinColumn(nullable = false)
    @OneToOne
    private HousingDetails housingDetails;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private Image previewImage;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Location location;

    @Column(nullable = false)
    private String coordinates;

    @Column(nullable = false, columnDefinition = "DECIMAL(8,2)")
    private BigDecimal pricePerNight;

    @Column(nullable = false)
    private Integer people;

    @Column(nullable = false, columnDefinition = "DECIMAL(3,1)")
    private BigDecimal rating;

    @JsonIgnore
    @OneToMany
    private List<Booking> bookings;
}