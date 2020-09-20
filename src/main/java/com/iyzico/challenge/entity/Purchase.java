package com.iyzico.challenge.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Purchase {
    @Id
    @GeneratedValue
    private Long id;
    private Long productId;
    private Integer productCount;

}
