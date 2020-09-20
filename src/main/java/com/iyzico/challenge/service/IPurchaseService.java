package com.iyzico.challenge.service;

import com.iyzico.challenge.request.PurchaseRequest;
import com.iyzico.challenge.response.ProductResponse;

public interface IPurchaseService {
    ProductResponse buyProduct(PurchaseRequest purchaseRequest);
}
