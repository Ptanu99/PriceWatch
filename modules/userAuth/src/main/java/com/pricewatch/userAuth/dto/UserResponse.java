package com.pricewatch.userAuth.dto;

import lombok.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String name;
}
