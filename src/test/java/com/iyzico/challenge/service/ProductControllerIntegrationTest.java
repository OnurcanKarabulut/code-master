package com.iyzico.challenge.service;

import com.iyzico.challenge.Application;
import com.iyzico.challenge.request.ProductRequest;
import com.iyzico.challenge.response.ProductResponse;
import com.iyzico.challenge.service.Impl.ProductServiceImpl;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@EnableAutoConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerIntegrationTest {

    @Mock
    private ProductServiceImpl productService;


    private ProductRequest productRequest;
    private ProductResponse productResponse;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Before
    public void setup() {
        productRequest = new ProductRequest("mobile phone", "Iphone 11", 2, BigDecimal.valueOf(7500));
        productResponse = new ProductResponse(1L, "mobile phone", "Iphone 11", 2, BigDecimal.valueOf(7500));
        when(productService.addProduct(productRequest)).thenReturn(productResponse);
    }

    @Test
    public void addProductTest() {
        ResponseEntity<ProductResponse> responseEntity = restTemplate
                .postForEntity("http://localhost:" + port + "/product/addProduct", productRequest, ProductResponse.class);
        ProductResponse actualResponse = responseEntity.getBody();
        assertEquals(actualResponse.getId(), productResponse.getId());
    }

    @Test
    public void listProductsTest() {
        List<ProductResponse> productList = new ArrayList<>();
        productList.add(new ProductResponse(1L, "Asus Computer", "Computer", 12, new BigDecimal(3000)));
        productList.add(new ProductResponse(2L, "Mac", "Computer", 15, new BigDecimal(10000)));
        productList.add(new ProductResponse(3L, "Hp", "Computer", 20, new BigDecimal(2500)));
        when(productService.listProduct()).thenReturn(productList);
        ResponseEntity<Object[]> responseEntity = restTemplate
                .getForEntity("http://localhost:" + port + "/product/listProducts", Object[].class);
        List<Object> actualResponse = Arrays.asList(responseEntity.getBody());

        for (int i = 0; i < actualResponse.size(); i++) {
            assertEquals(productList.get(i), (actualResponse.get(i)));
        }
    }

}
