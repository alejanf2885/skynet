package com.alejanf.skynet.controller;

import com.alejanf.skynet.dto.ProductDTO;
import com.alejanf.skynet.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthProducts {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(@NonNull Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }
}
