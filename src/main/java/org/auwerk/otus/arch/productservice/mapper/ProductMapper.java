package org.auwerk.otus.arch.productservice.mapper;

import java.util.List;

import org.auwerk.otus.arch.productservice.api.dto.AvailableProductDto;
import org.auwerk.otus.arch.productservice.api.dto.ProductDto;
import org.auwerk.otus.arch.productservice.domain.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface ProductMapper {

    ProductDto toDto(Product product);

    AvailableProductDto toAvailableDto(Product product);

    List<AvailableProductDto> toAvailableDtos(List<Product> products);
}
