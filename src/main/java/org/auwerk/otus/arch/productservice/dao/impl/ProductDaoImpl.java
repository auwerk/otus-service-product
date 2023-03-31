package org.auwerk.otus.arch.productservice.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.enterprise.context.ApplicationScoped;

import org.auwerk.otus.arch.productservice.dao.ProductDao;
import org.auwerk.otus.arch.productservice.domain.Product;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;

@ApplicationScoped
public class ProductDaoImpl implements ProductDao {

    @Override
    public Uni<Product> findByCode(PgPool pool, String code) {
        return pool.preparedQuery("SELECT * FROM products WHERE code=$1")
                .execute(Tuple.of(code))
                .map(rowSet -> {
                    final var rowSetIterator = rowSet.iterator();
                    if (!rowSetIterator.hasNext()) {
                        throw new NoSuchElementException("product not found, code=" + code);
                    }
                    return mapRow(rowSetIterator.next());
                });
    }

    @Override
    public Uni<List<Product>> findAllAvailable(PgPool pool, int page, int pageSize) {
        return pool.preparedQuery("SELECT * FROM products WHERE available IS TRUE LIMIT $1 OFFSET $2")
                .execute(Tuple.of(pageSize, pageSize * (page - 1)))
                .map(rowSet -> {
                    final var result = new ArrayList<Product>(rowSet.rowCount());
                    final var rowSetIterator = rowSet.iterator();
                    while (rowSetIterator.hasNext()) {
                        result.add(mapRow(rowSetIterator.next()));
                    }
                    return result;
                });
    }

    private static Product mapRow(Row row) {
        return Product.builder()
                .code(row.getString("code"))
                .name(row.getString("name"))
                .description(row.getString("description"))
                .available(row.getBoolean("available"))
                .price(row.getBigDecimal("price"))
                .build();
    }
}
