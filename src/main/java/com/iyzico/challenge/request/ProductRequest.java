package com.iyzico.challenge.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.repository.NoRepositoryBean;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ProductRequest {

    private String name;
    private String description;
    private Integer stockCount;
    private BigDecimal price;

}
