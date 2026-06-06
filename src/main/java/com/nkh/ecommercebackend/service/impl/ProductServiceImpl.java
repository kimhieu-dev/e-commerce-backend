package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.common.InventoryStatus;
import com.nkh.ecommercebackend.dto.request.CreateProductReq;
import com.nkh.ecommercebackend.dto.request.ProductFilterReq;
import com.nkh.ecommercebackend.dto.request.UpdateProductReq;
import com.nkh.ecommercebackend.dto.response.ProductOverviewStats;
import com.nkh.ecommercebackend.dto.response.ProductRes;
import com.nkh.ecommercebackend.entity.Inventory;
import com.nkh.ecommercebackend.dto.response.InventoryRes;
import com.nkh.ecommercebackend.entity.Product;
import com.nkh.ecommercebackend.entity.ProductDetail;
import com.nkh.ecommercebackend.exception.BusinessException;
import com.nkh.ecommercebackend.exception.ErrorCode;
import com.nkh.ecommercebackend.mapper.InventoryMapper;
import com.nkh.ecommercebackend.mapper.ProductMapper;
import com.nkh.ecommercebackend.repository.CategoryRepo;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final CategoryRepo categoryRepo;

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

        if (request.getMinPrice()!=null){
            specification = specification.and(ProductSpec.greatThanOrEqualTo(request.getMinPrice()));
        }

        if (request.getMaxPrice()!=null){
            specification = specification.and(ProductSpec.lessThanOrEqualTo(request.getMaxPrice()));
        }

        if (request.getCategoryId() != null && !request.getCategoryId().isEmpty()) {
            specification = specification.and(ProductSpec.equalCategoryId(request.getCategoryId()));
        }

        Page<Product> products = productRepo.findAll(specification, pageable);
        List<Product> productList = products.getContent();
        return productMapper.toProductResList(productList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductRes createProduct(CreateProductReq request) {
        //1. validate sku exist

        Boolean checkSku = productRepo.existsBySku(request.getSku());
        if (checkSku) {
            throw new BusinessException(ErrorCode.SKU_EXISTED);
        }
        Product product = Product.builder()
                .sku(request.getSku())
                .name(request.getName())
                .basePrice(request.getBasePrice())
                .thumbnailUrl(request.getThumbnailUrl())
                .build();
        
        if (request.getCategoryId() != null && !request.getCategoryId().isEmpty()) {
            product.setCategory(categoryRepo.findById(request.getCategoryId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND)));
        }
        
        productRepo.save(product);

        ProductDetail productDetail = ProductDetail.builder()
                .product(product)
                .description(request.getDescription())
                .weight(request.getWeight())
                .length(request.getLength())
                .width(request.getWidth())
                .height(request.getHeight())
                .build();
        productDetailRepo.save(productDetail);

        Inventory inventory = Inventory.builder()
                .product(product)
                .quantityInStock(request.getQuantityInStock())
                .reservedQuantity(request.getReservedQuantity())
                .build();
        if (request.getQuantityInStock() == 0) {
            inventory.setStatus(InventoryStatus.OUT_OF_STOCK);
        } else if (request.getQuantityInStock() <= 10) {
            inventory.setStatus(InventoryStatus.LIMITED_STOCK);
        } else {
            inventory.setStatus(InventoryStatus.IN_STOCK);
        }
        inventoryRepo.save(inventory);
        return productMapper.toProductRes(product);
    }

    @Override
    public ProductOverviewStats getOverview(LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null) fromDate = LocalDate.now().minusDays(30);
        if (toDate == null) toDate = LocalDate.now();

        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        LocalDateTime toDateTime = toDate.atStartOfDay();

        return productRepo.getOverviewStats(fromDateTime, toDateTime);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductRes updateProduct(String id, UpdateProductReq request) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        product.setName(request.getName());
        product.setBasePrice(request.getBasePrice());
        product.setThumbnailUrl(request.getThumbnailUrl());
        
        if (request.getCategoryId() != null && !request.getCategoryId().isEmpty()) {
            product.setCategory(categoryRepo.findById(request.getCategoryId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND)));
        } else {
            product.setCategory(null);
        }
        
        productRepo.save(product);


        ProductDetail productDetail = product.getProductDetail();
        productDetail.setProduct(product);
        productDetail.setDescription(request.getDescription());
        productDetail.setWeight(request.getWeight());
        productDetail.setLength(request.getLength());
        productDetail.setWidth(request.getWidth());
        productDetail.setHeight(request.getHeight());
        productDetailRepo.save(productDetail);

        Inventory inventory = product.getInventory();
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

        return productMapper.toProductRes(product);
    }

    @Override
    public void deleteProduct(String id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
        product.setDeleted(true);
        productRepo.save(product);
    }
}
