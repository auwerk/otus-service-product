package org.auwerk.otus.arch.productservice.service;

import java.util.List;

import org.auwerk.otus.arch.productservice.domain.Product;

import io.smallrye.mutiny.Uni;

public interface ProductService {

    Uni<Product> getProductByCode(String productCode);

    Uni<List<Product>> getAvailableProducts(int page, int pageSize);
}
