package org.auwerk.otus.arch.productservice.api.dto;

import java.math.BigDecimal;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RegisterForReflection
public class AvailableProductDto {
    private String code;
    private String name;
    private String description;
    private BigDecimal price;
}
