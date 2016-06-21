package com.epages.docs.exercise;

import com.neovisionaries.i18n.CurrencyCode;
import lombok.Data;
import lombok.Singular;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.epages.docs.exercise.OptionalStream.PaymentStatus.CANCELLED;
import static com.epages.docs.exercise.OptionalStream.PaymentStatus.OPEN;
import static com.epages.docs.exercise.OptionalStream.PaymentStatus.PAID;
import static com.neovisionaries.i18n.CurrencyCode.EUR;

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
            throw new UnsupportedOperationException();
        }

        /**
         * Get all Orders paid in the given currency.
         */
        public List<Order> getAllPaidOrders(CurrencyCode currency) {
            throw new UnsupportedOperationException();
        }

        /**
         * Get a customer's orders by paymentStatus status.
         */
        public Map<PaymentStatus, List<Order>> getOrdersByStatus(String customerName) {
            throw new UnsupportedOperationException();
        }

        /**
         * Get all customers that have cancelled at least one order
         */
        public List<Customer> getHappyCustomers() {
            throw new UnsupportedOperationException();
        }

        /**
         * Validates if the Order contains any Products that are not available in that currency.
         */
        public boolean validateOrder() {
            throw new UnsupportedOperationException();
        }

        /**
         * Get the total amount in EUR for all open orders.
         * Make sure you take care of invalid orders,
         *
         * Try not to use getOrDefault, you have a new best friend.
         */
        public long getOutstandingAmount() {
            throw new UnsupportedOperationException();
        }

    }

}
