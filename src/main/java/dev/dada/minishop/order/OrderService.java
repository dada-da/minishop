package dev.dada.minishop.order;

import dev.dada.minishop.cart.Cart;
import dev.dada.minishop.cart.CartItem;
import dev.dada.minishop.cart.CartRepository;
import dev.dada.minishop.exception.BusinessException;
import dev.dada.minishop.product.Product;
import dev.dada.minishop.product.ProductRepository;
import dev.dada.minishop.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

/**
 * ===== TRAI TIM CUA PROJECT - TASK MS-18 + MS-21 =====
 *
 * placeOrder(userId):
 *   1. Lay cart cua user, validate khong rong
 *   2. Voi moi item: kiem tra ton kho, tru stockQuantity
 *   3. Tao Order + OrderItem (snapshot gia)
 *   4. Tao Payment (PENDING)
 *   5. Xoa cart
 *   => TAT CA trong 1 @Transactional. Payment fail -> rollback tru kho.
 *
 * MS-21 (concurrency): khi tru kho, @Version tren Product gay
 * OptimisticLockException neu 2 nguoi mua cung luc -> bat va bao "het hang/thu lai".
 */
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void placeOrder(Long userId) {
        // TODO MS-18, MS-21
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new BusinessException("Can not find cart with user"));

        if (cart.getCartItems().isEmpty()) {
            throw new BusinessException("Cart is empty");
        }

        Order order = new Order();
        order.setUserId(userId);
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal totalOriginal = BigDecimal.ZERO;
        ArrayList<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getCartItems()) {
            Product product = productRepository.findById(cartItem.getProduct().getId()).orElseThrow(()  -> new BusinessException("Product not found"));

            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new BusinessException("Product stock quantity exceeded");
            } else {
                product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitOriginalPrice(cartItem.getProduct().getOriginalPrice());
            orderItem.setUnitPrice(cartItem.getProduct().getPrice());

            if (cartItem.getProduct().getOriginalPrice() != null) {
                totalOriginal = totalOriginal.add(cartItem.getProduct().getOriginalPrice().multiply(BigDecimal.valueOf(cartItem.getProduct().getStockQuantity())));
            }

            orderItems.add(orderItem);
            totalPrice = totalPrice.add(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getProduct().getStockQuantity())));
        }

        order.setTotalAmount(totalPrice);
        order.setOriginalTotalAmount(totalOriginal.equals(BigDecimal.ZERO) ? null : totalOriginal);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderItems(orderItems);

        orderRepository.save(order);
    }
}
