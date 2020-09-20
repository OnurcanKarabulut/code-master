package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.request.ProductRequest;
import com.iyzico.challenge.response.ProductResponse;

import java.util.List;

public interface IProductService {
    ProductResponse addProduct(ProductRequest productRequest);

    ProductResponse deleteProduct(Long productId);

    List<ProductResponse> listProduct();

    ProductResponse updateProduct(Long id, ProductRequest ProductRequest);
}
