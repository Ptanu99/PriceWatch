package com.pricewatch.project.wishlist.dto;

import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
public class CreateWishlistItemRequest {
    private String productUrl;
    private String title;
    private Double targetPrice;
}
