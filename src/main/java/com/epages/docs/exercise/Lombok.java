package com.epages.docs.exercise;

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

    static class Product {
        private String name;
        private String description;
        private Price price;
    }

    static class LineItem {
        private Product product;
        private Integer amount;
    }

    static class Order {
        private LocalDateTime createdAt;
        private List<LineItem> lineItems;
    }
}
