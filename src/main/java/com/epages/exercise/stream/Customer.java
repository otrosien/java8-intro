package com.epages.exercise.stream;

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