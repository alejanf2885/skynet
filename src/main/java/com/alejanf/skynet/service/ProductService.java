package com.alejanf.skynet.service;

import com.alejanf.skynet.dto.ProductDTO;
import com.alejanf.skynet.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    
    public Page<ProductDTO> getAllProducts(@NonNull Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductDTO::fromEntity);
    }
}
