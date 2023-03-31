package org.auwerk.otus.arch.productservice.exception;

import lombok.Getter;

public class ProductNotFoundException extends RuntimeException {

    @Getter
    private final String productCode;

    public ProductNotFoundException(String productCode) {
        super("product not found, code=" + productCode);
        this.productCode = productCode;
    }
}
