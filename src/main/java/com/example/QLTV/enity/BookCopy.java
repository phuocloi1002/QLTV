package com.example.QLTV.enity;

import com.example.QLTV.enity.enums.BookCondition;
import com.example.QLTV.enity.enums.BookStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Types;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "book_copy")
public class BookCopy extends BaseEntity {

    @Id
    @UuidGenerator
    @JdbcTypeCode(Types.VARCHAR)
    @Column(name = "id", columnDefinition = "CHAR(36)")
    UUID id;

    @Column(unique = true)
    String barcode;

    @Enumerated(EnumType.STRING)
    @Column(name = "circulation_status")
    BookStatus circulationStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition_status")
    BookCondition conditionStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    Book book;

    @OneToMany(mappedBy = "bookCopy")
    List<Loan> loans;

    @OneToMany(mappedBy = "bookCopy")
    List<Reservation> reservations;
}