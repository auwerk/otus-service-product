package org.auwerk.otus.arch.productservice.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import javax.enterprise.context.ApplicationScoped;

import org.auwerk.otus.arch.productservice.dao.ProductDao;
import org.auwerk.otus.arch.productservice.domain.Product;
import org.auwerk.otus.arch.productservice.exception.ProductNotFoundException;
import org.auwerk.otus.arch.productservice.service.ProductService;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final PgPool pool;
    private final ProductDao productDao;

    @Override
    public Uni<Product> getProductByCode(String productCode) {
        return productDao.findByCode(pool, productCode)
                .onFailure(NoSuchElementException.class)
                .transform(ex -> new ProductNotFoundException(productCode));
    }

    @Override
    public Uni<List<Product>> getAvailableProducts(int page, int pageSize) {
        return productDao.findAllAvailable(pool, page, pageSize);
    }
}
