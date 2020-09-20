package com.iyzico.challenge.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.OK, reason = "Out of product.Please Try again later")
public class OutOfStockException extends RuntimeException {
}
