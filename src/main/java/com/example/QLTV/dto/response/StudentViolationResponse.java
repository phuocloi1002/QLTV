package com.example.QLTV.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentViolationResponse {
    UUID id;
    String fullName;
    Double totalDebt;
    List<FineResponse> finesDetails;
}
