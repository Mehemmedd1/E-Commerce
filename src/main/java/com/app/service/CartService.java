package com.app.service;

import com.app.model.Cart;
import com.app.model.User;

public interface CartService {
    Cart getOrCreateCart(User user);
    Cart addToCart(User user, Long productId, int quantity);
    Cart removeFromCart(User user, Long productId);
    Cart updateCartItemQuantity(User user, Long productId, int newQuantity);
    void clearCart(User user);
}