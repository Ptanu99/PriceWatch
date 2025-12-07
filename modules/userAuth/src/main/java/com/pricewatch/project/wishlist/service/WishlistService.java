package com.pricewatch.project.wishlist.service;

import com.pricewatch.project.userAuth.entity.User;
import com.pricewatch.project.userAuth.repository.UserRepository;
import com.pricewatch.project.wishlist.Exceptions.ForbiddenException;
import com.pricewatch.project.wishlist.Exceptions.ResourceNotFoundException;
import com.pricewatch.project.wishlist.dto.WishlistItemResponse;
import com.pricewatch.project.wishlist.dto.WishlistResponse;
import com.pricewatch.project.wishlist.entity.Wishlist;
import com.pricewatch.project.wishlist.entity.WishlistItem;
import com.pricewatch.project.wishlist.repository.WishlistItemRepository;
import com.pricewatch.project.wishlist.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.NoPermissionException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;
    @Autowired
    private WishlistItemRepository wishlistItemRepository;
    @Autowired
    private UserRepository userRepository; // to fetch User for relation


    @Transactional
    public WishlistResponse createWishlist(Long userId, String name) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Wishlist w = new Wishlist();
        w.setUser(user);
        w.setName(name);
        w = wishlistRepository.save(w);

        return mapToWishlistResponse(w, false);
    }

    private WishlistResponse mapToWishlistResponse(Wishlist w, boolean includeItems) {
        List<WishlistItemResponse> items = includeItems
                ? w.getItems().stream().map(this::mapToWishlistItemResponse).collect(Collectors.toList())
                : null;

        return new WishlistResponse(
                w.getId(),
                w.getName(),
                w.getUser() != null ? w.getUser().getId() : null,
                items
        );
    }

    private WishlistItemResponse mapToWishlistItemResponse(WishlistItem item) {
        return new WishlistItemResponse(
                item.getId(),
                item.getProductUrl(),
                item.getTitle(),
                item.getTargetPrice()
        );
    }

    @Transactional(readOnly = true)
    public List<WishlistResponse> listWishlists(Long userId, int page, int size) {
        var pageReq = PageRequest.of(Math.max(0, page), Math.max(1, size));
        return wishlistRepository.findByUserId(userId, pageReq)
                .stream()
                .map(w -> mapToWishlistResponse((Wishlist) w, false)) // without items to keep it light
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public WishlistResponse getWishlistWithItems(Long userId, Long wishlistId) {
        Wishlist w = wishlistRepository.findByIdAndUserId(wishlistId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found for this user"));

        // force load items if lazy (we access them)
        w.getItems().size();

        return mapToWishlistResponse(w, true);
    }

    @Transactional
    public WishlistItemResponse addItemToWishlist(Long userId, Long wishlistId,
                                                  String productUrl, String title, Double targetPrice) {

        Wishlist w = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new NullPointerException("Wishlist not found"));

        if (!w.getUser().getId().equals(userId)) {
            throw new NullPointerException("Not allowed to add items to this wishlist");
        }

        WishlistItem item = new WishlistItem();
        item.setProductUrl(productUrl);
        item.setTitle(title);
        item.setTargetPrice(BigDecimal.valueOf(targetPrice));
        // set wishlist relationship
        item.setWishlist(w);

        item = wishlistItemRepository.save(item);

        // optionally add to wishlist.items in-memory
        w.getItems().add(item);

        return mapToWishlistItemResponse(item);
    }

    @Transactional
    public void deleteWishlist(Long userId, Long wishlistId) throws NoPermissionException {
        Wishlist w = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found"));
        if (!w.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Not allowed to delete this wishlist");
        }
        wishlistRepository.delete(w);
    }

    @Transactional(readOnly = true)
    public List<WishlistItemResponse> listItems(Long userId, Long wishlistId) {
        Wishlist w = wishlistRepository.findByIdAndUserId(wishlistId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found for this user"));

        List<WishlistItem> items = wishlistItemRepository.findByWishlistId(w.getId());
        return items.stream().map(this::mapToWishlistItemResponse).collect(Collectors.toList());
    }

    @Transactional
    public WishlistItemResponse updateItem(Long userId, Long wishlistId, Long itemId,
                                           String title, Double targetPrice) throws NoPermissionException {

        Wishlist w = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found"));

        if (!w.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Not allowed to update items on this wishlist");
        }

        WishlistItem item = wishlistItemRepository.findByIdAndWishlistId(itemId, wishlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found in wishlist"));

        if (title != null) item.setTitle(title);
        if (targetPrice != null) item.setTargetPrice(BigDecimal.valueOf(targetPrice));

        item = wishlistItemRepository.save(item);
        return mapToWishlistItemResponse(item);
    }

    @Transactional
    public void removeItem(Long userId, Long wishlistId, Long itemId) throws NoPermissionException {
        Wishlist w = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new NullPointerException("Wishlist not found"));
//                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found"));

        if (!w.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Not allowed to remove items from this wishlist");
        }

        WishlistItem item = wishlistItemRepository.findByIdAndWishlistId(itemId, wishlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found in wishlist"));

        wishlistItemRepository.delete(item);
    }

}
