package com.epages.docs.streams;

import java.util.List;

import lombok.Data;
import lombok.NonNull;
import lombok.Singular;

@Data class Customer {
    @NonNull
    final String name;

    @Singular
    @NonNull
    final List<Order> orders;
}