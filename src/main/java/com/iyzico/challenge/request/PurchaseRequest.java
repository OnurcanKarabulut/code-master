package com.iyzico.challenge.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PurchaseRequest {

    private Long productId;
    private Integer productCount;

}
