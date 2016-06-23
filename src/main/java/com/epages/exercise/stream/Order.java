package com.epages.exercise.stream;

import java.time.LocalDateTime;
import java.util.List;

import com.neovisionaries.i18n.CurrencyCode;

import lombok.Data;
import lombok.Singular;

@Data class Order {

    enum PaymentStatus {
        OPEN,
        PAID,
        CANCELLED
    }

    private final LocalDateTime createdAt;
    @Singular
    private final List<LineItem> lineItems;
    private final CurrencyCode currency;
    private final PaymentStatus paymentStatus;

    public boolean isPaid() {
        return PaymentStatus.PAID.equals(paymentStatus);
    }
}