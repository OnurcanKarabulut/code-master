package com.iyzico.challenge.controller;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.request.ProductRequest;
import com.iyzico.challenge.response.ProductResponse;
import com.iyzico.challenge.service.IProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }


    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductRequest productRequest) {
        return new ResponseEntity<>(productService.addProduct(productRequest), HttpStatus.ACCEPTED);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> listProducts() {
        return new ResponseEntity<>(productService.listProduct(), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<ProductResponse> deleteProduct(@RequestParam Long productId) {
        return new ResponseEntity<>(productService.deleteProduct(productId), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ProductResponse> updateProduct(@RequestParam Long productId, @RequestBody ProductRequest productRequest) {
        return new ResponseEntity<>(productService.updateProduct(productId, productRequest), HttpStatus.OK);
    }

}
