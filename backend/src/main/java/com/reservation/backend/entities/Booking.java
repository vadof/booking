package com.reservation.backend.entities;

import com.reservation.backend.config.Constants;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne
    private User tenant;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Housing housing;

    @Column(nullable = false)
    @DateTimeFormat(pattern = Constants.DATE_FORMAT_DD_MM_YYYY)
    private LocalDate checkInDate;

    @Column(nullable = false)
    @DateTimeFormat(pattern = Constants.DATE_FORMAT_DD_MM_YYYY)
    private LocalDate checkOutDate;

    @Column(nullable = false)
    private Integer nights;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private BigDecimal totalPrice;

    @Column(columnDefinition = "TEXT")
    private String additionalInfo;
}
