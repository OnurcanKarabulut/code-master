package com.iyzico.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.OK, reason = "Product Not found")
public class ProductNotFoundException extends RuntimeException {

}
