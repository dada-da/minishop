package dev.dada.minishop.product;

import dev.dada.minishop.common.ApiResponse;
import dev.dada.minishop.product.dto.ProductRequest;
import dev.dada.minishop.product.dto.ProductDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TASK MS-07: GET /api/products (public, co phan trang + filter), GET /api/products/{id}.
 * POST/PUT/DELETE /api/products yeu cau ADMIN.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {
    // TODO MS-07, MS-25
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ApiResponse<ProductDto> createProduct(@Valid @RequestBody ProductRequest product) {
        return ApiResponse.ok(productService.addProduct(product));
    }

    @GetMapping("/all")
    public ApiResponse<List<ProductDto>> getAll() {
        return ApiResponse.ok(productService.getAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductDto> getById(@PathVariable Long id) {
        return ApiResponse.ok(productService.getById(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductDto> update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return ApiResponse.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<ProductDto> deleteById(@PathVariable Long id) {
        return ApiResponse.ok(productService.deleteById(id));
    }
}
