package com.iyzico.challenge.service.Impl;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.entity.Purchase;
import com.iyzico.challenge.exception.OutOfStockException;
import com.iyzico.challenge.exception.ProductNotFoundException;
import com.iyzico.challenge.mapper.ProductMapper;
import com.iyzico.challenge.repository.ProductRepository;
import com.iyzico.challenge.repository.PurchaseRepository;
import com.iyzico.challenge.request.PurchaseRequest;
import com.iyzico.challenge.response.ProductResponse;
import com.iyzico.challenge.service.IPurchaseService;
import com.iyzico.challenge.service.IyzicoPaymentService;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class PurchaseServiceImpl implements IPurchaseService {

    private ProductRepository productRepository;
    private ProductMapper mapper
            = Mappers.getMapper(ProductMapper.class);
    private IyzicoPaymentService iyzicoPaymentService;
    Lock lock;
    private PurchaseRepository purchaseRepository;
    private Logger logger = LoggerFactory.getLogger(PurchaseServiceImpl.class);

    public PurchaseServiceImpl(ProductRepository productRepository, IyzicoPaymentService iyzicoPaymentService, PurchaseRepository purchaseRepository) {
        this.productRepository = productRepository;
        this.iyzicoPaymentService = iyzicoPaymentService;
        this.lock = new ReentrantLock();
        this.purchaseRepository = purchaseRepository;
    }


    @Override
    public ProductResponse buyProduct(PurchaseRequest purchaseRequest) {
        Purchase purchase = new Purchase();
        Product product = productRepository.findById(purchaseRequest.getProductId()).orElseThrow(ProductNotFoundException::new);
        try {
            lock.lock();
            if (product.getStockCount() >= purchaseRequest.getProductCount()) {
                BigDecimal price = product.getPrice().multiply(new BigDecimal(purchaseRequest.getProductCount()));
                iyzicoPaymentService.pay(price);
                product.setStockCount(product.getStockCount() - purchaseRequest.getProductCount());
                Product purchasedProduct = productRepository.save(product);
                logger.info("Product id: {} purchased! And its stockCount decreased {}", product.getId(), purchaseRequest.getProductCount());
                purchase.setProductId(purchaseRequest.getProductId());
                purchase.setProductCount(purchaseRequest.getProductCount());
                purchaseRepository.save(purchase);
                logger.info("Product id: {} purchased!", product.getId());
                return ProductMapper.map.productToProductResponse(purchasedProduct);
            } else {
                throw new OutOfStockException();
            }
        } finally {
            lock.unlock();
        }

    }
}
