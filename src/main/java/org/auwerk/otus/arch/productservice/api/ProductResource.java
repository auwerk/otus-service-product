package org.auwerk.otus.arch.productservice.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.auwerk.otus.arch.productservice.exception.ProductNotFoundException;
import org.auwerk.otus.arch.productservice.mapper.ProductMapper;
import org.auwerk.otus.arch.productservice.service.ProductService;

import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class ProductResource {

    protected static final String DEFAULT_PAGE = "1";
    protected static final String DEFAULT_PAGE_SIZE = "10";

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GET
    @Path("/{productCode}")
    public Uni<Response> getByCode(@PathParam("productCode") String productCode) {
        return productService.getProductByCode(productCode)
                .map(product -> Response.ok(productMapper.toDto(product)).build())
                .onFailure(ProductNotFoundException.class)
                .recoverWithItem(failure -> Response.status(Status.NOT_FOUND)
                        .entity(failure.getMessage()).build())
                .onFailure()
                .recoverWithItem(failure -> Response.serverError().entity(failure.getMessage()).build());
    }

    @GET
    public Uni<Response> getAvailable(@QueryParam("page") @DefaultValue(DEFAULT_PAGE) int page,
            @QueryParam("pageSize") @DefaultValue(DEFAULT_PAGE_SIZE) int pageSize) {
        return productService.getAvailableProducts(page, pageSize)
                .map(products -> Response.ok(productMapper.toAvailableDtos(products)).build())
                .onFailure()
                .recoverWithItem(failure -> Response.serverError().entity(failure.getMessage()).build());
    }
}
