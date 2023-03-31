package org.auwerk.otus.arch.productservice.domain;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {
    private String code;
    private String name;
    private String description;
    private Boolean available;
    private BigDecimal price;
}
