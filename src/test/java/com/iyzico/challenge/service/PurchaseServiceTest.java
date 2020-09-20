package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.exception.OutOfStockException;
import com.iyzico.challenge.exception.ProductNotFoundException;
import com.iyzico.challenge.repository.ProductRepository;
import com.iyzico.challenge.repository.PurchaseRepository;
import com.iyzico.challenge.request.PurchaseRequest;
import com.iyzico.challenge.response.ProductResponse;
import com.iyzico.challenge.service.Impl.ProductServiceImpl;
import com.iyzico.challenge.service.Impl.PurchaseServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import java.math.BigDecimal;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
@RestClientTest(ProductServiceImpl.class)
public class PurchaseServiceTest {

    @InjectMocks
    private PurchaseServiceImpl purchaseService;

    @Mock
    private ProductRepository productRepository;

    @Test
    public void buyProductSuccess() {
        Product product = new Product(1L, "Asus Computer", "Computer", 12, new BigDecimal(3000));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        PurchaseRequest request = new PurchaseRequest(1L, 2);

        Product response = new Product(1L, "Asus Computer", "Computer", 10, new BigDecimal(3000));
        when(productRepository.save(product)).thenReturn(response);

        ProductResponse actualResponse = purchaseService.buyProduct(request);
        assertEquals(response.getStockCount(), actualResponse.getStockCount());
    }

    @Test(expected = OutOfStockException.class)
    public void buyProductFailOutOfStock() {
        Product product = new Product(1L, "Asus Computer", "Computer", 1, new BigDecimal(3000));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        PurchaseRequest request = new PurchaseRequest(1L, 2);
        ProductResponse actualResponse = purchaseService.buyProduct(request);
    }

    @Test(expected = ProductNotFoundException.class)
    public void buyProductFailProductNotFound() {
        Product product = new Product(1L, "Asus Computer", "Computer", 1, new BigDecimal(3000));
        when(productRepository.findById(product.getId())).thenReturn(Optional.empty());
        PurchaseRequest request = new PurchaseRequest(1L, 2);
        ProductResponse actualResponse = purchaseService.buyProduct(request);
    }
}
