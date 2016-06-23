package com.epages.docs.streams;

import lombok.Data;

@Data class LineItem {
    private final Product product;
    private final long amount;
}