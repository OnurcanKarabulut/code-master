package com.iyzico.challenge.mapper;


import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.request.ProductRequest;
import com.iyzico.challenge.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper map = Mappers.getMapper(ProductMapper.class);

    Product productRequestToProduct(ProductRequest productRequest);

    ProductResponse productToProductResponse(Product product);
}
