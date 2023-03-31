package org.auwerk.otus.arch.productservice.dao;

import java.util.List;

import org.auwerk.otus.arch.productservice.domain.Product;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;

public interface ProductDao {

    Uni<Product> findByCode(PgPool pool, String code);

    Uni<List<Product>> findAllAvailable(PgPool pool, int page, int pageSize);
}
