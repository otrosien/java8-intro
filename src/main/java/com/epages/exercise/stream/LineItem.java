package com.epages.exercise.stream;

import lombok.Data;

@Data class LineItem {
    private final Product product;
    private final long amount;
}