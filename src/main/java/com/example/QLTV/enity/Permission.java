package com.example.QLTV.enity;

import com.example.QLTV.enity.enums.PermissionName;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "permission")
public class Permission extends BaseEntity {

    @Id
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "id", columnDefinition = "CHAR(36)")
    UUID id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    PermissionName name;

    String description;

    @ManyToMany(mappedBy = "permissions")
    Set<Role> roles;
}