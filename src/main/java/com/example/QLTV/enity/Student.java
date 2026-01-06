package com.example.QLTV.enity;

import com.example.QLTV.enity.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Student extends BaseEntity {
    @Id
    @UuidGenerator
    @Column(name = "id", columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    UUID id;

    @Column(unique = true, nullable = false)
    String studentCode;

    String faculty;
    String clazz;

    @Enumerated(EnumType.STRING)
    UserStatus status;

    @Column(name = "fine_balance")
    Double fineBalance = 0.0;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "student")
    List<Loan> loans;

    @OneToMany(mappedBy = "student")
    List<Reservation> reservations;

    @OneToMany(mappedBy = "student")
    List<Review> reviews;
}