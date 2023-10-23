package com.reservation.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reservation.backend.config.Constants;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

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

    @DateTimeFormat(pattern = Constants.DATE_FORMAT_DD_MM_YYYY)
    @Column(nullable = false)
    private LocalDate date;

    @JoinColumn(nullable = false)
    @ManyToOne
    private User reviewer;

    @JsonIgnore
    @JoinColumn(nullable = false)
    @ManyToOne
    private Housing housing;

    @PrePersist
    private void setDate() {
        if (this.id == null) {
            this.date = LocalDate.now();
        }
    }

}
