package com.epages.docs.example.lombok;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

// defined outside because of visibility

@Getter
@Setter
class Product {
    private String name;
    @Setter(AccessLevel.PRIVATE)
    private boolean visible = false;
}
