package com.epages.exercise.example.lombok;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.junit.Test;

public class LombokExamples {

    @Test
    public void getter_setter() {
        Product product = new Product();
        product.setName("Deuter Backpack 2000");
        product.isVisible();
        // product.setVisible(false);         // visible setter is private
    }

    @Test
    public void getter_setter2() {
        Product product = new Product();
        product.setName("Deuter Backpack 2000");
        product.isVisible();
        // product.setVisible(false);         // visible setter is private
    }


    @RequiredArgsConstructor
    static class ProductRequiredArgs {
        private final String sku;
        private String name;
        private boolean purchasable;
        @NonNull
        private Integer stockLevel;
    }

    @Test
    public void requiredArgs() {
        new ProductRequiredArgs("Towel", 42);
    }


}