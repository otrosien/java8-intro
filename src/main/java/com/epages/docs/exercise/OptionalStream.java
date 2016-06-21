package com.epages.docs.exercise;

import com.google.common.collect.Maps;
import com.neovisionaries.i18n.CurrencyCode;
import lombok.Data;
import lombok.Singular;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.epages.docs.exercise.OptionalStream.PaymentStatus.CANCELLED;
import static com.epages.docs.exercise.OptionalStream.PaymentStatus.OPEN;
import static com.epages.docs.exercise.OptionalStream.PaymentStatus.PAID;
import static com.neovisionaries.i18n.CurrencyCode.EUR;
import static java.util.stream.Collectors.toList;

/**
 * Variation of the model used in Lombok.
 * Implement the methods using OptionalStream and Optional.
 */
class OptionalStream {
    private OptionalStream() {}

    // data model

    @Data
    static class Product {
        private String name;
        private String description;
        private Map<CurrencyCode, Long> prices;
    }

    @Data
    static class LineItem {
        private Product product;
        private long amount;
    }

    enum PaymentStatus {
        OPEN,
        PAID,
        CANCELLED
    }

    @Data
    static class Order {
        private final LocalDateTime createdAt;
        @Singular
        private final List<LineItem> lineItems;
        private final CurrencyCode currency;
        private final PaymentStatus paymentStatus;

        public boolean isPaid() {
            return PAID.equals(paymentStatus);
        }
    }

    @Data
    static class Customer {
        String name;

        @Singular
        List<Order> orders;
    }



    static class CustomerService {

        public List<Customer> findAll() {
            return new ArrayList<>();
        }

        /**
         * Find a customer by name.
         * Assume that customer names are unique.
         */
        Optional<Customer> findByName(String name) {
            return findAll().stream()
                    .filter(c -> c.getName().equals(name))
                    .findFirst();
        }

        /**
         * Get all Orders paid in the given currency.
         */
        public List<Order> getAllPaidOrders(CurrencyCode currency) {
            return findAll().stream()
                    .flatMap(o -> o.getOrders().stream())
                    .filter(Order::isPaid)
                    .filter(o -> currency.equals(o.getCurrency()))
                    .collect(toList());
        }

        /**
         * Get a customer's orders by paymentStatus status.
         */
        public Map<PaymentStatus, List<Order>> getOrdersByStatus(String customerName) {
            return findByName(customerName)
                    .map(c -> c.getOrders().stream()
                            .collect(Collectors.groupingBy(Order::getPaymentStatus)))
                    .orElse(Maps.newHashMap());
        }

        /**
         * Get all customers that have cancelled at least one order
         */
        public List<Customer> getHappyCustomers() {
            return findAll().stream()
                    .filter(c -> c.getOrders().stream()
                            .noneMatch(p -> CANCELLED.equals(p.getPaymentStatus())))
                    .collect(toList());
        }

        /**
         * Validates if the Order contains any Products that are not available in that currency.
         */
        public boolean validateOrder() {
            return findAll().stream()
                    .flatMap(c -> c.getOrders().stream())
                    .map(o -> o.getLineItems().stream()
                            .flatMap(li -> li.getProduct().getPrices().keySet().stream())
                            .anyMatch(cc -> o.getCurrency().equals(cc)))
                    .allMatch(id -> id);
        }

        /**
         * Get the total amount in EUR for all open orders.
         * Make sure you take care of invalid orders,
         *
         * Try not to use getOrDefault, you have a new best friend.
         */
        public long getOutstandingAmount() {
            return findAll().stream()
                    .flatMap(c -> c.getOrders().stream())
                    .filter(o -> OPEN.equals(o.getPaymentStatus()))
                    .filter(o -> EUR.equals(o.getCurrency()))
                    .flatMap(p -> p.getLineItems().stream())
                    //.mapToLong(li -> li.getProduct().getPrices().getOrDefault(EUR, 0L))
                    .mapToLong(li -> Optional.ofNullable(li.getProduct().getPrices().get(EUR)).orElse(0L))
                    .sum();
        }

    }

}
