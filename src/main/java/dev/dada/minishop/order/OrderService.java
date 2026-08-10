package dev.dada.minishop.order;

import dev.dada.minishop.cart.Cart;
import dev.dada.minishop.cart.CartItem;
import dev.dada.minishop.cart.CartRepository;
import dev.dada.minishop.common.PageResponse;
import dev.dada.minishop.exception.BusinessException;
import dev.dada.minishop.order.dto.ChangeStatusRequest;
import dev.dada.minishop.order.dto.OrderDto;
import dev.dada.minishop.order.dto.OrderItemDto;
import dev.dada.minishop.product.Product;
import dev.dada.minishop.product.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * ===== TRAI TIM CUA PROJECT - TASK MS-18 + MS-21 =====
 * <p>
 * placeOrder(userId):
 * 1. Lay cart cua user, validate khong rong
 * 2. Voi moi item: kiem tra ton kho, tru stockQuantity
 * 3. Tao Order + OrderItem (snapshot gia)
 * 4. Tao Payment (PENDING)
 * 5. Xoa cart
 * <p>
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
    public OrderDto placeOrder(Long userId) {
        // TODO MS-18, MS-21
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new BusinessException("Can not find cart with user"));

        if (cart.getCartItems().isEmpty()) {
            throw new BusinessException("Cart is empty");
        }

        Order order = new Order();
        order.setUserId(userId);
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal totalOriginal = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = getOrderItem(cartItem, order);

            if (cartItem.getProduct().getOriginalPrice() != null) {
                totalOriginal = totalOriginal.add(cartItem.getProduct().getOriginalPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            }
            totalPrice = totalPrice.add(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));

            order.addItem(orderItem);
        }

        order.setTotalAmount(totalPrice);
        order.setOriginalTotalAmount(totalOriginal.compareTo(BigDecimal.ZERO) == 0 ? null : totalOriginal);
        order.setOrderStatus(OrderStatus.PENDING);

        orderRepository.save(order);
        List<CartItem> cartItems = cart.getCartItems();
        cartItems.clear();

        return toResponseDto(order);
    }

    @Transactional(readOnly = true)
    public PageResponse<OrderDto> getOrders(int page, int size, String sortBy, String direction, Long userId) {
        if (!Set.of("id", "createdAt", "updatedAt").contains(sortBy)) {
            sortBy = "id";
        }

        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Order> orderPage = orderRepository.findByUserId(userId, pageable);

        List<OrderDto> orders = new ArrayList<>();

        for (Order order : orderPage.getContent()) {
            OrderDto orderDto = toResponseDto(order);
            orders.add(orderDto);
        }

        return new PageResponse<>(orders, page, size, orderPage.getTotalElements(), orderPage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long orderId, Long userId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId).orElseThrow(() -> new BusinessException("Order not found"));

        return toResponseDto(order);
    }

    @Transactional
    public OrderDto changeOrderStatus(Long orderId, ChangeStatusRequest request) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new BusinessException("Order not found"));

        OrderStatus status = order.getOrderStatus();

        if (!status.canTransitionTo(request.status())) {
            throw new BusinessException("Order status cannot transition to " + request.status());
        }

        order.setOrderStatus(request.status());

        if (request.status().equals(OrderStatus.CANCELLED)) {
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                Product product = productRepository.findById(orderItem.getProductId()).orElseThrow(() -> new BusinessException("Product not found"));

                product.setStockQuantity(product.getStockQuantity() + orderItem.getQuantity());
            }
        }

        return toResponseDto(order);
    }

    @Transactional
    public OrderDto cancelOrder(Long orderId, Long userId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId).orElseThrow(() -> new BusinessException("Order not found"));

        OrderStatus status = order.getOrderStatus();

        if (!status.canTransitionTo(OrderStatus.CANCELLED)) {
            throw new BusinessException("Cannot cancel order");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);

        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            Product product = productRepository.findById(orderItem.getProductId()).orElseThrow(() -> new BusinessException("Product not found"));

            product.setStockQuantity(product.getStockQuantity() + orderItem.getQuantity());
        }

        return toResponseDto(order);
    }

    private OrderDto toResponseDto(Order order) {
        List<OrderItemDto> orderItemDtoList = order.getOrderItems().stream().map(this::toResponseDto).toList();

        return new OrderDto(order.getId(), order.getOrderStatus().toString(), order.getTotalAmount(), order.getOriginalTotalAmount(), orderItemDtoList);
    }

    private OrderItemDto toResponseDto(OrderItem orderItem) {
        return new OrderItemDto(orderItem.getProductId(), orderItem.getProductName(), orderItem.getQuantity(), orderItem.getUnitPrice(), orderItem.getUnitOriginalPrice());
    }

    private static OrderItem getOrderItem(CartItem cartItem, Order order) {
        Product product = cartItem.getProduct();

        if (product.getStockQuantity() < cartItem.getQuantity()) {
            throw new BusinessException("Product stock quantity exceeded");
        } else {
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProductName(product.getName());
        orderItem.setProductId(product.getId());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setUnitOriginalPrice(cartItem.getProduct().getOriginalPrice());
        orderItem.setUnitPrice(cartItem.getProduct().getPrice());

        return orderItem;
    }
}
