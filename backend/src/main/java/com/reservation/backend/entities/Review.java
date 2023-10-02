package com.reservation.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(nullable = false)
    private Integer rating;

    @JoinColumn(nullable = false)
    @ManyToOne
    private User reviewer;

    @JsonIgnore
    @JoinColumn(nullable = false)
    @ManyToOne
    private Housing housing;

}
