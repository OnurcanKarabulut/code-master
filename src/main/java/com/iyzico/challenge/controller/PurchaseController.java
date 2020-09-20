package com.iyzico.challenge.controller;


import com.iyzico.challenge.request.PurchaseRequest;
import com.iyzico.challenge.response.ProductResponse;
import com.iyzico.challenge.service.IPurchaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    private IPurchaseService purchasingService;

    public PurchaseController(IPurchaseService purchasingService) {
        this.purchasingService = purchasingService;
    }


    @PostMapping
    public ResponseEntity<ProductResponse> buyProduct(@RequestBody PurchaseRequest purchaseRequest) {
        return new ResponseEntity<>(purchasingService.buyProduct(purchaseRequest), HttpStatus.ACCEPTED);
    }

}
