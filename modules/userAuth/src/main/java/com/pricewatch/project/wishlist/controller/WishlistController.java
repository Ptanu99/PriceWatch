package com.pricewatch.project.wishlist.controller;

import com.pricewatch.project.userAuth.entity.UserPrincipal;
import com.pricewatch.project.wishlist.dto.*;
import com.pricewatch.project.wishlist.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.naming.NoPermissionException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/wishlists")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping
    public ResponseEntity<WishlistResponse> createWishlist(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody CreateWishlistRequest request) {

        Long userId = principal.getUserId();
        WishlistResponse created = wishlistService.createWishlist(userId, request.getName());
        URI location = URI.create("/wishlists/" + created.getId().toString());
        return ResponseEntity.created(location).body(created);
    }

    /**
     * List authenticated user's wishlists (simple list, no items)
     * GET /wishlists
     */
    @GetMapping
    public ResponseEntity<List<WishlistResponse>> listWishlists(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Long userId = principal.getUserId();
        List<WishlistResponse> list = wishlistService.listWishlists(userId, page, size);
        return ResponseEntity.ok(list);
    }

    /**
     * Get wishlist with items. Only owner allowed.
     * GET /wishlists/{wishlistId}
     */
    @GetMapping("/{wishlistId}")
    public ResponseEntity<WishlistResponse> getWishlist(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long wishlistId) {

        Long userId = principal.getUserId();
        WishlistResponse resp = wishlistService.getWishlistWithItems(userId, wishlistId);
        return ResponseEntity.ok(resp);
    }

    /**
     * Delete a wishlist. Owner-only.
     * DELETE /wishlists/{wishlistId}
     */
    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<Void> deleteWishlist(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long wishlistId) throws NoPermissionException {

        Long userId = principal.getUserId();
        wishlistService.deleteWishlist(userId, wishlistId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Add item to wishlist.
     * POST /wishlists/{wishlistId}/items
     */
    @PostMapping("/{wishlistId}/items")
    public ResponseEntity<  WishlistItemResponse> addItem(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long wishlistId,
            @RequestBody CreateWishlistItemRequest request) {

        Long userId = principal.getUserId();
        WishlistItemResponse created = wishlistService.addItemToWishlist(userId, wishlistId,
                request.getProductUrl(), request.getTitle(), request.getTargetPrice());
        URI location = URI.create(String.format("/wishlists/%s/items/%s", wishlistId, created.getId()));
        return ResponseEntity.created(location).body(created);
    }

    /**
     * List items in wishlist.
     * GET /wishlists/{wishlistId}/items
     */
    @GetMapping("/{wishlistId}/items")
    public ResponseEntity<List<WishlistItemResponse>> listItems(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long wishlistId) {

        Long userId = principal.getUserId();
        List<WishlistItemResponse> items = wishlistService.listItems(userId, wishlistId);
        return ResponseEntity.ok(items);
    }

    /**
     * Update an item (partial update supported).
     * PUT /wishlists/{wishlistId}/items/{itemId}
     */
    @PutMapping("/{wishlistId}/items/{itemId}")
    public ResponseEntity<WishlistItemResponse> updateItem(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long wishlistId,
            @PathVariable Long itemId,
            @RequestBody UpdateWishlistItemRequest request) throws NoPermissionException {

        Long userId = principal.getUserId();
        WishlistItemResponse updated = wishlistService.updateItem(userId, wishlistId, itemId,
                request.getTitle(), request.getTargetPrice());
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete an item.
     * DELETE /wishlists/{wishlistId}/items/{itemId}
     */
    @DeleteMapping("/{wishlistId}/items/{itemId}")
    public ResponseEntity<Void> deleteItem(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long wishlistId,
            @PathVariable Long itemId) throws NoPermissionException {

        Long userId = principal.getUserId();
        wishlistService.removeItem(userId, wishlistId, itemId);
        return ResponseEntity.noContent().build();
    }

}
