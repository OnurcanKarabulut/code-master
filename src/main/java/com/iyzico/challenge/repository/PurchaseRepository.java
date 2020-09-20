package com.iyzico.challenge.repository;

import com.iyzico.challenge.entity.Payment;
import com.iyzico.challenge.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
