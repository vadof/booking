package com.reservation.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String contentType;

    @Lob
    @JdbcType(value = org.hibernate.type.descriptor.jdbc.BinaryJdbcType.class)
    private byte[] bytes;

    @ManyToOne
    private Housing housing;

}
