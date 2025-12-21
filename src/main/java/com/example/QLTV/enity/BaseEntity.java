package com.example.QLTV.enity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseEntity {

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "created_by", columnDefinition = "VARCHAR(36)")
    String createdBy;

    @Column(name = "updated_by", columnDefinition = "VARCHAR(36)")
    String updatedBy;

    @Column(name = "is_deleted", columnDefinition = "BIT(1) DEFAULT 0")
    Boolean isDeleted = false;
}