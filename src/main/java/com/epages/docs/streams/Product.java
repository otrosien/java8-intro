package com.epages.docs.streams;

import java.util.Map;

import com.neovisionaries.i18n.CurrencyCode;

import lombok.Data;

@Data class Product {
    private final String name;
    private final String description;
    private final Map<CurrencyCode, Long> prices;
}