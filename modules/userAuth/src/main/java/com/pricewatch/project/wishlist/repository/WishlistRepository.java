package com.pricewatch.project.wishlist.repository;

import com.pricewatch.project.wishlist.entity.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Wishlist save(Wishlist w);

    Page<Object> findByUserId(Long userId, PageRequest pageReq);

    Optional<Wishlist> findByIdAndUserId(Long wishlistId, Long userId);
}
