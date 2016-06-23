package com.epages.docs.exercise;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PUBLIC;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Singular;

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
    @Setter(PRIVATE)
    @RequiredArgsConstructor
    @EqualsAndHashCode
    static class Product {
        @NonNull
        private String name;
        @Setter(PUBLIC)
        private String description;
        private Price price;
    }

    @RequiredArgsConstructor
    @EqualsAndHashCode(of="product")
    static class LineItem {
        @NonNull
        private final Product product;
        @NonNull
        private final Integer amount;
    }

    @Builder
    @Getter
    static class Order {
        @NonNull
        private final LocalDateTime createdAt;
        @Singular
        private final List<LineItem> lineItems;

        static class OrderBuilder {
            OrderBuilder product(Product product) {
                lineItem(new LineItem(product, 1));
                return this;
            }
        }
    }
}
