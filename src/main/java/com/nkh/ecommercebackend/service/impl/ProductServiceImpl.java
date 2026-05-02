package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.common.InventoryStatus;
import com.nkh.ecommercebackend.dto.request.CreateProductReq;
import com.nkh.ecommercebackend.dto.request.ProductFilterReq;
import com.nkh.ecommercebackend.dto.response.ProductRes;
import com.nkh.ecommercebackend.entity.Inventory;
import com.nkh.ecommercebackend.entity.InventoryRes;
import com.nkh.ecommercebackend.entity.Product;
import com.nkh.ecommercebackend.entity.ProductDetail;
import com.nkh.ecommercebackend.exception.BusinessException;
import com.nkh.ecommercebackend.exception.ErrorCode;
import com.nkh.ecommercebackend.mapper.InventoryMapper;
import com.nkh.ecommercebackend.mapper.ProductMapper;
import com.nkh.ecommercebackend.repository.InventoryRepo;
import com.nkh.ecommercebackend.repository.ProductDetailRepo;
import com.nkh.ecommercebackend.repository.ProductRepo;
import com.nkh.ecommercebackend.service.ProductService;
import com.nkh.ecommercebackend.service.spec.ProductSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;
    private final InventoryRepo inventoryRepo;
    private final ProductDetailRepo productDetailRepo;
    private final InventoryMapper inventoryMapper;

    @Override
    public Product getProductById(String id) {
        Optional<Product> productOptional = productRepo.findById(id);
        if (productOptional.isEmpty()) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        return productOptional.get();
    }

    @Override
    public List<ProductRes> getProducts(ProductFilterReq request, Pageable pageable) {
        Specification<Product> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (request.getName() != null && !request.getName().isEmpty()) {
            specification = specification.and(ProductSpec.likeName(request.getName()));
        }

        if (request.getSku() != null && !request.getSku().isEmpty()) {
            specification = specification.and(ProductSpec.likeSku(request.getSku()));
        }
        Page<Product> products = productRepo.findAll(specification, pageable);
        List<Product> productList = products.getContent();
        List<ProductRes> productResList = productMapper.toProductResList(productList);

        return productResList;
    }

    @Override
    public ProductRes createProduct(CreateProductReq request) {
        Boolean checkSku = productRepo.existsBySku(request.getSku());
        if (checkSku) {
            throw new BusinessException(ErrorCode.SKU_EXISTED);
        }

        Product product = new Product();
        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setBasePrice(request.getBasePrice());
        product.setThumbnailUrl(request.getThumbnailUrl());
        productRepo.save(product);

        ProductDetail productDetail = new ProductDetail();
        productDetail.setProduct(product);
        productDetail.setDescription(request.getDescription());
        productDetail.setWeight(request.getWeight());
        productDetail.setLength(request.getLength());
        productDetail.setWidth(request.getWidth());
        productDetail.setHeight(request.getHeight());
        productDetailRepo.save(productDetail);

        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setQuantityInStock(request.getQuantityInStock());
        inventory.setReservedQuantity(request.getReservedQuantity());
        if (request.getQuantityInStock() == 0) {
            inventory.setStatus(InventoryStatus.OUT_OF_STOCK);
        } else if (request.getQuantityInStock() <= 10) {
            inventory.setStatus(InventoryStatus.LIMITED_STOCK);
        } else {
            inventory.setStatus(InventoryStatus.IN_STOCK);
        }
        inventoryRepo.save(inventory);

        InventoryRes inventoryRes = inventoryMapper.toInventoryRes(inventory);

        return ProductRes.builder()
                .sku(product.getSku())
                .name(product.getName())
                .basePrice(product.getBasePrice())
                .thumbnailUrl(product.getThumbnailUrl())
                .inventory(inventoryRes)
                .build();
    }
}
