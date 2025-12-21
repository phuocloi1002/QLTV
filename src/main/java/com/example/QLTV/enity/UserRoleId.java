package com.example.QLTV.enity;

import lombok.*;

import jakarta.persistence.Embeddable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode

public class UserRoleId implements Serializable {
    @JdbcTypeCode(SqlTypes.CHAR)
    UUID userId;
    @JdbcTypeCode(SqlTypes.CHAR)
    UUID roleId;
}