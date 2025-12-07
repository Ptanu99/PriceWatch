package com.pricewatch.project.wishlist.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "wishlist_items",
        indexes = {@Index(name = "idx_wishlistitem_wishlist", columnList = "wishlist_id")})
@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor
public class WishlistItem {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wishlist_id", referencedColumnName = "id", nullable = false)
    private Wishlist wishlist;

    @Column(name = "product_url", nullable = false, length = 2048)
    private String productUrl;

    private String title;

    @Column(name = "target_price", precision = 12, scale = 2)
    private BigDecimal targetPrice;

}
