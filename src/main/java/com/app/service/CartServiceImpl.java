package com.app.service;

import com.app.model.*;
import com.app.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;


    @Override
    @Transactional
    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user).orElseGet(()->{
            Cart cart=new Cart();
            cart.setUser(user);
            return cartRepository.save(cart);
        });
    }

    @Override
    @Transactional
    public Cart addToCart(User user, Long productId, int quantity) {
        Cart cart = getOrCreateCart(user);
        Product product=productRepository.
                findById(productId).
                orElseThrow(()->new RuntimeException("Məhsul tapılmadı"));
        Optional<CartItem> existingCartItem=cartItemRepository.findByCartAndProduct(cart,product);
        if(existingCartItem.isPresent()){
            CartItem item=existingCartItem.get();
            item.setQuantity(item.getQuantity()+quantity);
        }else {
            CartItem newItem=new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setPrice(product.getPrice());
            cartItemRepository.save(newItem);
        }
        return cart;
    }

    @Override
    @Transactional
    public Cart removeFromCart(User user, Long productId) {
        Cart cart = getOrCreateCart(user);
        Product product=productRepository.
                findById(productId).
                orElseThrow(()->new RuntimeException("Məhsul tapılmadı"));
        Optional<CartItem> existingCartItem=cartItemRepository.findByCartAndProduct(cart,product);
        existingCartItem.ifPresent(cartItemRepository::delete);
        return cart;
    }

    @Override
    @Transactional
    public Cart updateCartItemQuantity(User user, Long productId, int newQuantity) {
        if(newQuantity<=0){
            return removeFromCart(user,productId);
        }
        Cart cart = getOrCreateCart(user);
        Product product=productRepository.
                findById(productId).
                orElseThrow(()->new RuntimeException("Məhsul tapılmadı"));
        CartItem existingCartItem=cartItemRepository.
                findByCartAndProduct(cart,product).
                orElseThrow(()->new RuntimeException("Məhsul vəya səbət  tapılmadı"));
       existingCartItem.setQuantity(newQuantity);
       cartItemRepository.save(existingCartItem);
       return cart;

    }

    @Override
    @Transactional
    public void clearCart(User user) {
        Cart cart = getOrCreateCart(user);
        cartItemRepository.deleteByCart(cart);

    }
}