package com.pricewatch.project.wishlist.dto;

import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
public class UpdateWishlistItemRequest {
    private String title;
    private Double targetPrice;
}
