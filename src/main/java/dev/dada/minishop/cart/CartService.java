package dev.dada.minishop.cart;

import dev.dada.minishop.cart.dto.AddToCartRequest;
import dev.dada.minishop.cart.dto.CartDto;
import dev.dada.minishop.cart.dto.CartItemDto;
import dev.dada.minishop.cart.dto.UpdateCartItemRequest;
import dev.dada.minishop.exception.BusinessException;
import dev.dada.minishop.exception.ResourceNotFoundException;
import dev.dada.minishop.exception.UnprocessResourceException;
import dev.dada.minishop.product.Product;
import dev.dada.minishop.product.ProductRepository;
import dev.dada.minishop.user.User;
import dev.dada.minishop.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * TASK MS-15: addItem, updateQuantity, removeItem, getCart, clearCart.
 * Lay user tu SecurityContext (user chi thao tac gio cua chinh minh).
 */
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository) {

        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // TODO MS-15
    @Transactional(readOnly = true)
    public CartDto getCart(Long userId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);

        return cart.map(this::toResponseDto).orElseGet(() -> new CartDto(null, new ArrayList<>(), BigDecimal.ZERO));

    }

    @Transactional
    public CartDto addCartItem(AddToCartRequest request, Long userId) {
        Cart cart = findOrCreateCart(userId);

        List<CartItem> cartItems = cart.getCartItems();

        Optional<CartItem> existedCartItem = findCartItem(cartItems, request.productId());

        boolean exists = existedCartItem.isPresent();

        Product product;
        int currentQuantity = 0;

        if (exists) {
            product = existedCartItem.get().getProduct();
            currentQuantity = existedCartItem.get().getQuantity();
        } else {
            product = productRepository.findById(request.productId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        }


        int newQuantity = request.quantity() + currentQuantity;

        if (newQuantity > product.getStockQuantity()) {
            throw new UnprocessResourceException("Product quantity exceeds stock quantity");
        }

        if (exists) {
            increaseQuantity(existedCartItem.get(), request.quantity());
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(request.quantity());
            cartItem.setCart(cart);

            cartItems.add(cartItem);
        }

        return toResponseDto(cart);
    }

    @Transactional
    public CartDto updateCartItem(Long productId, UpdateCartItemRequest request, Long userId) {
        Cart cart = findCartOrThrow(userId);

        List<CartItem> cartItems = cart.getCartItems();

        CartItem existedCartItem = findCartItem(cartItems, productId).orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (request.quantity() <= 0) {
            cartItems.remove(existedCartItem);

            return toResponseDto(cart);
        }

        if (request.quantity() > existedCartItem.getProduct().getStockQuantity()) {
            throw new UnprocessResourceException("Cart item quantity exceeds stock quantity");
        }

        existedCartItem.setQuantity(request.quantity());

        return toResponseDto(cart);
    }

    @Transactional
    public CartDto removeCartItem(Long productId , Long userId) {
        Cart cart = findCartOrThrow(userId);

        List<CartItem> cartItems = cart.getCartItems();

        CartItem existedCartItem = findCartItem(cartItems, productId).orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        cartItems.remove(existedCartItem);

        return toResponseDto(cart);
    }

    @Transactional
    public CartDto clearCart(Long userId) {
        Cart cart = findCartOrThrow(userId);
        List<CartItem> cartItems = cart.getCartItems();

        cartItems.clear();

        return toResponseDto(cart);
    }

    private CartDto toResponseDto(Cart cart) {
        List<CartItemDto> cartItemDtoList = toCartItemsDto(cart.getCartItems());

        BigDecimal totalPrice = cartItemDtoList.stream().map(CartItemDto::lineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartDto(cart.getId(), cartItemDtoList, totalPrice);
    }

    private List<CartItemDto> toCartItemsDto(List<CartItem> cartItems) {
        return cartItems.stream().map(this::toCartItemDto).collect(Collectors.toList());
    }

    private CartItemDto toCartItemDto(CartItem cartItem) {
        BigDecimal price = cartItem.getProduct().getPrice();
        BigDecimal linePrice = price.multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        return new CartItemDto(
                cartItem.getProduct().getId(),
                cartItem.getProduct().getName(),
                cartItem.getQuantity(),
                price,
                cartItem.getProduct().getOriginalPrice(),
                linePrice
        );
    }

    private Cart createCart(Long userId) {
        Cart newCart = new Cart();
        newCart.setCartItems(new ArrayList<>());
        User user = userRepository.getReferenceById(userId);
        newCart.setUser(user);
        return cartRepository.save(newCart);
    }

    private Cart findOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> createCart(userId));
    }

    private Cart findCartOrThrow(Long userId) {
        return cartRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Can't find cart by userId"));
    }

    private void increaseQuantity(CartItem cartItem, Integer quantity) {
        Integer oldQuantity = cartItem.getQuantity();
        cartItem.setQuantity(oldQuantity + quantity);
    }

    private Optional<CartItem> findCartItem (List<CartItem> cartItems, Long productId) {
        return cartItems.stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findFirst();
    }
}
