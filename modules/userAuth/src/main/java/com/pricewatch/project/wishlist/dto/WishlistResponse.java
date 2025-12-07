package com.pricewatch.project.wishlist.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor @AllArgsConstructor
public class WishlistResponse {
    private Long id;
    private String name;
    private Long userId;
    private List<WishlistItemResponse> items;
}
