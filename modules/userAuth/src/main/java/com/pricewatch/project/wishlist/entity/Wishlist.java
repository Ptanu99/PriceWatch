package com.pricewatch.project.wishlist.entity;

import com.pricewatch.project.userAuth.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "wishlists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wishlist {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",              // column name in wishlists table
            referencedColumnName = "id",   // primary key in users table
            nullable = false
    )
    private User user;
    String name;

    @OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<WishlistItem> items = new ArrayList<>();

    // helper
    public void addItem(WishlistItem item) {
        items.add(item);
        item.setWishlist(this);
    }

    public void removeItem(WishlistItem item) {
        items.remove(item);
        item.setWishlist(null);
    }
}
