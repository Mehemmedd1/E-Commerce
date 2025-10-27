package com.app.controller;

import com.app.dto.CartItemRequest;
import com.app.model.Cart;
import com.app.model.Product;
import com.app.model.User;
import com.app.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;


    @GetMapping
    public ResponseEntity<Cart> getCart(@AuthenticationPrincipal User user) {
        Cart cart = cartService.getOrCreateCart(user);
        return ResponseEntity.ok(cart);
    }


    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(
            @AuthenticationPrincipal User user,
            @RequestBody CartItemRequest request
    ) {
        Cart cart = cartService.addToCart(user, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(cart);
    }


    @PutMapping("/update")
    public ResponseEntity<Cart> updateCartItem(
            @AuthenticationPrincipal User user,
            @RequestBody CartItemRequest request
    ) {
        Cart cart = cartService.updateCartItemQuantity(user, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(cart);
    }


    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Cart> removeCartItem(
            @AuthenticationPrincipal User user,
            @PathVariable Long productId
    ) {
        Cart cart = cartService.removeFromCart(user, productId);
        return ResponseEntity.ok(cart);
    }


    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(@AuthenticationPrincipal User user) {
        cartService.clearCart(user);
        return ResponseEntity.ok("Bütün səbət silindi");
    }
}