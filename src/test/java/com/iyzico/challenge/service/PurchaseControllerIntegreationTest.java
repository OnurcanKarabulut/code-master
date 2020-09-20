package com.iyzico.challenge.service;

import com.iyzico.challenge.Application;
import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.repository.ProductRepository;
import com.iyzico.challenge.request.ProductRequest;
import com.iyzico.challenge.request.PurchaseRequest;
import com.iyzico.challenge.response.ProductResponse;
import com.iyzico.challenge.service.Impl.PurchaseServiceImpl;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@EnableAutoConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PurchaseControllerIntegreationTest {


    @Mock
    private PurchaseServiceImpl purchaseService;

    @Mock
    private ProductRepository productRepository;
    @Autowired
    private TestRestTemplate restTemplate = new TestRestTemplate();

    @LocalServerPort
    private int port;

    private PurchaseRequest purchaseRequest;
    private ProductResponse productResponse;
    private ProductRequest productRequest;
    private Product product;

    @Before
    public void setup() {
        productRequest = new ProductRequest("mobile phone", "Iphone 11", 2, BigDecimal.valueOf(7500));

        productResponse = new ProductResponse(1L, "Asus Computer", "Computer", 12, new BigDecimal(3000));
        product = new Product(1L, "Asus Computer", "Computer", 12, new BigDecimal(3000));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        purchaseRequest = new PurchaseRequest(1L, 2);
        when(purchaseService.buyProduct(purchaseRequest)).thenReturn(productResponse);
    }

    @Test
    public void buyNotFoundProductTest() throws JSONException {

        ResponseEntity<String> responseEntity = restTemplate
                .postForEntity("http://localhost:" + port + "/purchase/buyproduct", purchaseRequest, String.class);
        String message = responseEntity.getBody();
        JSONObject messageJson = new JSONObject(message);
        assertEquals("Product Not found", messageJson.get("message"));
    }

    @Test
    public void buyProductTest() {
        ResponseEntity<ProductResponse> addProductResponse = restTemplate
                .postForEntity("http://localhost:" + port + "/product/addProduct", productRequest, ProductResponse.class);
        ProductResponse addedProduct = addProductResponse.getBody();

        ResponseEntity<ProductResponse> purchaseResponse = restTemplate
                .postForEntity("http://localhost:" + port + "/purchase/buyproduct", purchaseRequest, ProductResponse.class);
        ProductResponse response = purchaseResponse.getBody();
        assertEquals(response.getStockCount().toString(), String.valueOf(addedProduct.getStockCount() - productRequest.getStockCount()));
    }

    @Test
    public void buyOutOfStockProductTest() throws JSONException {
        ResponseEntity<ProductResponse> addProductResponse = restTemplate
                .postForEntity("http://localhost:" + port + "/product/addProduct", productRequest, ProductResponse.class);
        ProductResponse addedProduct = addProductResponse.getBody();

        purchaseRequest = new PurchaseRequest(1L, 4);
        ResponseEntity<String> responseEntity = restTemplate
                .postForEntity("http://localhost:" + port + "/purchase/buyproduct", purchaseRequest, String.class);
        String message = responseEntity.getBody();
        JSONObject messageJson = new JSONObject(message);
        assertEquals("Out of product.Please Try again later", messageJson.get("message"));
    }
}
