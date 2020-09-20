package com.iyzico.challenge.service.Impl;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.exception.ProductNotFoundException;
import com.iyzico.challenge.mapper.ProductMapper;
import com.iyzico.challenge.repository.ProductRepository;
import com.iyzico.challenge.request.ProductRequest;
import com.iyzico.challenge.response.ProductResponse;
import com.iyzico.challenge.service.IProductService;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements IProductService {


    private ProductRepository repository;

    private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private ProductMapper mapper
            = Mappers.getMapper(ProductMapper.class);

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductResponse addProduct(ProductRequest productRequest) {
        Product product = mapper.productRequestToProduct(productRequest);
        repository.save(product);
        logger.info("Product id: {} saved!", product.getId());
        return mapper.productToProductResponse(product);
    }

    @Override
    public ProductResponse deleteProduct(Long productId) {
        Product product = repository.findById(productId).orElseThrow(ProductNotFoundException::new);
        repository.delete(product);
        logger.info("Product id: {} deleted!", product.getId());
        return mapper.productToProductResponse(product);
    }


    @Override
    public List<ProductResponse> listProduct() {
        return repository.findAll().stream().map(product -> mapper.productToProductResponse(product)).collect(Collectors.toList());
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product product = repository.findById(id).orElseThrow(ProductNotFoundException::new);
        product.setDescription(productRequest.getDescription());
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setStockCount(productRequest.getStockCount());
        repository.save(product);
        logger.info("Product id: {} updated!", product.getId());
        return mapper.productToProductResponse(product);
    }
}
