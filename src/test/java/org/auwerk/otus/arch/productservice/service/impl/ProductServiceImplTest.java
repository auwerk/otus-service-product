package org.auwerk.otus.arch.productservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.NoSuchElementException;

import org.auwerk.otus.arch.productservice.dao.ProductDao;
import org.auwerk.otus.arch.productservice.domain.Product;
import org.auwerk.otus.arch.productservice.exception.ProductNotFoundException;
import org.auwerk.otus.arch.productservice.service.ProductService;
import org.junit.jupiter.api.Test;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.vertx.mutiny.pgclient.PgPool;

public class ProductServiceImplTest {

    private final PgPool pool = mock(PgPool.class);
    private final ProductDao productDao = mock(ProductDao.class);
    private final ProductService productService = new ProductServiceImpl(pool, productDao);

    @Test
    void getByProductCode_success() {
        // given
        final var productCode = "PRODUCT1";
        final var product = Product.builder().build();

        // when
        when(productDao.findByCode(pool, productCode))
                .thenReturn(Uni.createFrom().item(product));
        final var subscriber = productService.getProductByCode(productCode).subscribe()
                .withSubscriber(UniAssertSubscriber.create());

        // then
        subscriber.assertItem(product);
    }

    @Test
    void getByProductCode_productNotFound() {
        // given
        final var productCode = "PRODUCT1";

        // when
        when(productDao.findByCode(pool, productCode))
                .thenReturn(Uni.createFrom().failure(new NoSuchElementException()));
        final var subscriber = productService.getProductByCode(productCode).subscribe()
                .withSubscriber(UniAssertSubscriber.create());

        // then
        final var failure = (ProductNotFoundException) subscriber
                .assertFailedWith(ProductNotFoundException.class)
                .getFailure();
        assertEquals(productCode, failure.getProductCode());
    }

    @Test
    void getAvailableProducts_success() {
        // given
        final var page = 1;
        final var pageSize = 10;
        final var products = List.of(
                Product.builder().build(),
                Product.builder().build(),
                Product.builder().build());

        // when
        when(productDao.findAllAvailable(pool, page, pageSize))
                .thenReturn(Uni.createFrom().item(products));
        final var subscriber = productService.getAvailableProducts(page, pageSize).subscribe()
                .withSubscriber(UniAssertSubscriber.create());

        // then
        subscriber.assertItem(products);
    }
}
