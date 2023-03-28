package org.auwerk.otus.arch.productservice.api;

import static org.mockito.ArgumentMatchers.anyInt;

import java.math.BigDecimal;
import java.util.List;

import org.auwerk.otus.arch.productservice.domain.Product;
import org.auwerk.otus.arch.productservice.exception.ProductNotFoundException;
import org.auwerk.otus.arch.productservice.service.ProductService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.RestAssured;
import io.smallrye.mutiny.Uni;

@QuarkusTest
@TestHTTPEndpoint(ProductResource.class)
public class ProductResourceTest {

    private static final String PRODUCT_CODE = "PRODUCT1";

    @InjectMock
    ProductService productService;

    @Test
    void getByCode_success() {
        // given
        final var product = Product.builder()
                .code(PRODUCT_CODE)
                .name("Product")
                .description("Product description")
                .available(true)
                .price(BigDecimal.valueOf(500))
                .build();

        // when
        Mockito.when(productService.getProductByCode(PRODUCT_CODE))
                .thenReturn(Uni.createFrom().item(product));

        // then
        RestAssured.given()
                .get("/{productCode}", PRODUCT_CODE)
                .then()
                .statusCode(200)
                .and()
                .body("code", Matchers.is(product.getCode()))
                .body("name", Matchers.is(product.getName()))
                .body("description", Matchers.is(product.getDescription()))
                .body("available", Matchers.is(product.getAvailable()))
                .body("price", Matchers.comparesEqualTo(product.getPrice().intValue()));
    }

    @Test
    void getByCode_productNotFound() {
        // when
        Mockito.when(productService.getProductByCode(PRODUCT_CODE))
                .thenReturn(Uni.createFrom().failure(new ProductNotFoundException(PRODUCT_CODE)));

        // then
        RestAssured.given()
                .get("/{productCode}", PRODUCT_CODE)
                .then()
                .statusCode(404);
    }

    @Test
    void getByCode_serverError() {
        // when
        Mockito.when(productService.getProductByCode(PRODUCT_CODE))
                .thenReturn(Uni.createFrom().failure(new RuntimeException()));

        // then
        RestAssured.given()
                .get("/{productCode}", PRODUCT_CODE)
                .then()
                .statusCode(500);
    }

    @Test
    void getAvailable_success() {
        // given
        final var page = 1;
        final var pageSize = 10;

        // when
        Mockito.when(productService.getAvailableProducts(page, pageSize))
                .thenReturn(Uni.createFrom().item(List.of()));

        // then
        RestAssured.given()
                .param("page", page)
                .param("pageSize", pageSize)
                .get()
                .then()
                .statusCode(200);
    }

    @Test
    void getAvailable_defaultPageParams() {
        // given
        final var page = 1;
        final var pageSize = 10;

        // when
        Mockito.when(productService.getAvailableProducts(page, pageSize))
                .thenReturn(Uni.createFrom().item(List.of()));

        // then
        RestAssured.given()
                .get()
                .then()
                .statusCode(200);

        Mockito.verify(productService, Mockito.times(1))
                .getAvailableProducts(page, pageSize);
    }

    @Test
    void getAvailable_serverError() {
        // when
        Mockito.when(productService.getAvailableProducts(anyInt(), anyInt()))
                .thenReturn(Uni.createFrom().failure(new RuntimeException()));

        // then
        RestAssured.given()
                .get()
                .then()
                .statusCode(500);
    }
}
