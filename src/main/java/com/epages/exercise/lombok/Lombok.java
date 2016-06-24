package com.epages.exercise.lombok;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Add modifiers and Lombok annotations to fulfill the following requirements
 *
 * - Product
 *   - Getters
 *   - Setters
 *     - only the description setter is public, all others are private
 *   - name field cannot be null
 * - LineItem
 *   - is immutable
 *   - fields cannot be null
 * - Order
 *   - is immutable
 *   - can only be created using a builder
 *   - does not accept null values
 *   - has a method "lineItem" for adding a single lineItem
 *   - bonus: has a custom builder method "product" which accepts a Product and creates a LineItem with quantity 1
 *
 * Try to use as few annotations as possible.
 */
class Lombok {
    private Lombok() {}


    static class Price {
    }

    @Getter
    @Setter(AccessLevel.PRIVATE)
    @RequiredArgsConstructor
    @EqualsAndHashCode
    static class Product {
        @NonNull
        private String name;
        @Setter(AccessLevel.PUBLIC)
        private String description;
        private Price price;
    }

    // @Value also possible
    @RequiredArgsConstructor
    @EqualsAndHashCode(of = "product")
    static class LineItem {
        @NonNull
        private final Product product;
        @NonNull
        private final Integer amount;
    }

    @Builder
    @Getter
    @ToString
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class Order {
        @NonNull
        private final LocalDateTime createdAt;
        @NonNull
        @Singular
        private final List<LineItem> lineItems;

        static class OrderBuilder {
            OrderBuilder() {
                createdAt(LocalDateTime.now());
            }
            public OrderBuilder product(@NonNull Product product) {
                lineItem(new LineItem(product, 1));
                return this;
            }
        }
    }
}
