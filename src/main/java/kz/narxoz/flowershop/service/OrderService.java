package kz.narxoz.flowershop.service;

import kz.narxoz.flowershop.dto.order.CreateOrderRequest;
import kz.narxoz.flowershop.dto.order.OrderItemResponse;
import kz.narxoz.flowershop.dto.order.OrderResponse;
import kz.narxoz.flowershop.entity.*;
import kz.narxoz.flowershop.exception.BadRequestException;
import kz.narxoz.flowershop.exception.NotFoundException;
import kz.narxoz.flowershop.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final InventoryRepository inventoryRepository;
    private final PaymentRepository paymentRepository;
    private final DeliveryRepository deliveryRepository;

    public OrderService(CartRepository cartRepository,
                        CartItemRepository cartItemRepository,
                        AddressRepository addressRepository,
                        OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        InventoryRepository inventoryRepository,
                        PaymentRepository paymentRepository,
                        DeliveryRepository deliveryRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.addressRepository = addressRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.inventoryRepository = inventoryRepository;
        this.paymentRepository = paymentRepository;
        this.deliveryRepository = deliveryRepository;
    }

    @Transactional
    public OrderResponse createOrder(Long userId, CreateOrderRequest req) {
        var cart = cartRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("Cart not found"));
        var items = cartItemRepository.findByCartId(cart.getId());
        if (items.isEmpty()) throw new BadRequestException("Cart is empty");

        Address address = addressRepository.findById(req.getAddressId())
                .orElseThrow(() -> new NotFoundException("Address not found"));
        if (!address.getUser().getId().equals(userId)) throw new NotFoundException("Address not found");

        // validate stock
        for (var it : items) {
            var inv = inventoryRepository.findByProductId(it.getProduct().getId())
                    .orElseThrow(() -> new NotFoundException("Inventory not found"));
            if (it.getQuantity() > inv.getQuantity()) {
                throw new BadRequestException("Not enough stock for: " + it.getProduct().getName());
            }
        }

        BigDecimal total = items.stream()
                .map(i -> i.getProduct().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUser(address.getUser());
        order.setDeliveryAddress(address);
        order.setStatus("NEW");
        order.setTotalAmount(total);
        order = orderRepository.save(order);

        for (var it : items) {
            var inv = inventoryRepository.findByProductId(it.getProduct().getId()).orElseThrow();
            inv.setQuantity(inv.getQuantity() - it.getQuantity());

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(it.getProduct());
            oi.setQuantity(it.getQuantity());
            oi.setPriceAtPurchase(it.getProduct().getPrice());
            orderItemRepository.save(oi);
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setMethod(req.getPaymentMethod().toUpperCase());
        payment.setAmount(total);
        payment.setStatus("PENDING");
        paymentRepository.save(payment);

        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setStatus("PREPARING");
        deliveryRepository.save(delivery);

        cartItemRepository.deleteByCartId(cart.getId());

        return buildResponse(order);
    }

    public List<OrderResponse> listForUser(Long userId) {
        return orderRepository.findByUserId(userId).stream().map(this::buildResponse).toList();
    }

    public OrderResponse getOne(Long userId, Long orderId) {
        Order o = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        if (!o.getUser().getId().equals(userId)) throw new NotFoundException("Order not found");
        return buildResponse(o);
    }

    @Transactional
    public OrderResponse markPaid(Long orderId) {
        Order o = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        var payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new NotFoundException("Payment not found"));
        payment.setStatus("SUCCESS");
        o.setStatus("PAID");
        return buildResponse(o);
    }

    private OrderResponse buildResponse(Order order) {
        var items = orderItemRepository.findByOrderId(order.getId()).stream().map(i ->
                new OrderItemResponse(
                        i.getId(),
                        i.getProduct().getId(),
                        i.getProduct().getName(),
                        i.getQuantity(),
                        i.getPriceAtPurchase()
                )
        ).toList();

        String payStatus = paymentRepository.findByOrderId(order.getId()).map(Payment::getStatus).orElse("N/A");
        String delStatus = deliveryRepository.findByOrderId(order.getId()).map(Delivery::getStatus).orElse("N/A");

        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getDeliveryAddress().getId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                items,
                payStatus,
                delStatus
        );
    }
}
