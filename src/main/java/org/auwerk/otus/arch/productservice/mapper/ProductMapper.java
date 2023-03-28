package org.auwerk.otus.arch.productservice.mapper;

import java.util.List;

import org.auwerk.otus.arch.productservice.api.dto.ProductDto;
import org.auwerk.otus.arch.productservice.domain.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface ProductMapper {

    ProductDto toDto(Product product);

    List<ProductDto> toDtos(List<Product> products);
}
