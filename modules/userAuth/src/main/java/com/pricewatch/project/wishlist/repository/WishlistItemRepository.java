package com.pricewatch.project.wishlist.repository;

import com.pricewatch.project.wishlist.entity.Wishlist;
import com.pricewatch.project.wishlist.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findByWishlistId(Long id);

    Optional<WishlistItem> findByIdAndWishlistId(Long itemId, Long wishlistId);
//    Optional<WishlistItem> findByIdAndWishlistId(Long id, Long wishlistId);
}
