package com.pricewatch.project.wishlist.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor @AllArgsConstructor
public class WishlistItemResponse {
    private Long id;
    private String productUrl;
    private String title;
    private BigDecimal targetPrice;
}
