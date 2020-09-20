package com.iyzico.challenge.service;


import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.exception.ProductNotFoundException;
import com.iyzico.challenge.mapper.ProductMapper;

import com.iyzico.challenge.repository.ProductRepository;
import com.iyzico.challenge.request.ProductRequest;
import com.iyzico.challenge.response.ProductResponse;
import com.iyzico.challenge.service.Impl.ProductServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.when;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RunWith(MockitoJUnitRunner.class)
@RestClientTest(ProductServiceImpl.class)
public class ProductServiceTest {


    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductRequest productRequest;

    @Before
    public void initialize() {
        productRequest = new ProductRequest("mobile phone", "Iphone 11", 2, BigDecimal.valueOf(7500));
    }

    @Test
    public void addProductTest() {
        ProductResponse response = productService.addProduct(productRequest);
        assertEquals("mobile phone", response.getName());
        assertEquals("Iphone 11", response.getDescription());
        assertEquals(BigDecimal.valueOf(7500), response.getPrice());
    }

    @Test
    public void findAllProductTest() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product(1L, "Asus Computer", "Computer", 12, new BigDecimal(3000)));
        productList.add(new Product(2L, "Mac", "Computer", 15, new BigDecimal(10000)));
        productList.add(new Product(3L, "Hp", "Computer", 20, new BigDecimal(2500)));
        when(productRepository.findAll()).thenReturn(productList);
        List<ProductResponse> list = productList.stream().map(product -> ProductMapper.map.productToProductResponse(product)).collect(Collectors.toList());

        List<ProductResponse> products = productService.listProduct();
        for (int i = 0; i < productList.size(); i++) {
            assertEquals(products.get(i).getId(), (list.get(i).getId()));
        }
    }

    @Test
    public void deleteProductByIdSuccess() {
        Long id = 1L;
        Product product = new Product(1L, "Asus Computer", "Computer", 12, new BigDecimal(3000));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse actualProduct = ProductMapper.map.productToProductResponse(product);
        ProductResponse response = productService.deleteProduct(id);

        assertEquals(actualProduct.getId(), response.getId());
    }

    @Test(expected = ProductNotFoundException.class)
    public void removeProductWithWrongId() {
        Long id = 3L;
        when(productRepository.findById(id)).thenReturn(Optional.empty());
        productService.deleteProduct(id);
    }

}
