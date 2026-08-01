package dev.dada.minishop.cart;

import dev.dada.minishop.cart.dto.AddToCartRequest;
import dev.dada.minishop.cart.dto.CartDto;
import dev.dada.minishop.cart.dto.CartItemDto;
import dev.dada.minishop.exception.BusinessException;
import dev.dada.minishop.product.Product;
import dev.dada.minishop.product.ProductRepository;
import dev.dada.minishop.user.User;
import dev.dada.minishop.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        Cart cart = findCartByUserId(userId);

        if (Objects.isNull(cart)) {
            return new CartDto(null, new ArrayList<>(), BigDecimal.ZERO);
        }

        return toResponseDto(cart);
    }

    @Transactional
    public CartDto addCartItem(AddToCartRequest request, Long userId) {
        Cart cart = Objects.requireNonNullElseGet(findCartByUserId(userId), () -> createCart(userId));

        List<CartItem> cartItems = cart.getCartItems();

        Optional<CartItem> existedCartItem = cartItems.stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(request.productId()))
                .findFirst();

        boolean exists = existedCartItem.isPresent();

        Product product;
        int currentQuantity = 0;

        if (exists) {
            product = existedCartItem.get().getProduct();
            currentQuantity = existedCartItem.get().getQuantity();
        } else {
            product = productRepository.findById(request.productId()).orElseThrow(() -> new BusinessException("Product not found"));
        }


        int newQuantity = request.quantity() + currentQuantity;

        if (newQuantity > product.getStockQuantity()) {
            throw new BusinessException("Product quantity exceeds stock quantity");
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

    private Cart findCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId).orElse(null);
    }

    private Cart createCart(Long userId) {
        Cart newCart = new Cart();
        newCart.setCartItems(new ArrayList<>());
        User user = userRepository.getReferenceById(userId);
        newCart.setUser(user);
        return cartRepository.save(newCart);
    }

    private void increaseQuantity(CartItem cartItem, Integer quantity) {
        Integer oldQuantity = cartItem.getQuantity();
        cartItem.setQuantity(oldQuantity + quantity);
    }
}
