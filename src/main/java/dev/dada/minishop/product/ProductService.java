package dev.dada.minishop.product;

import dev.dada.minishop.category.Category;
import dev.dada.minishop.category.CategoryRepository;
import dev.dada.minishop.exception.BusinessException;
import dev.dada.minishop.product.dto.ProductRequest;
import dev.dada.minishop.product.dto.ProductDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * TASK MS-07: CRUD product.
 * TASK MS-25: search + filter + pagination (Pageable + Specification).
 */
@Service
public class ProductService {
    // TODO MS-07, MS-25
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductDto addProduct(ProductRequest request) {
        Product product = new Product();

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new BusinessException("Category not found"));

            product.setCategory(category);
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());

        product.setOriginalPrice(request.getOriginalPrice());

        return toResponseDto(productRepository.save(product));
    }

    public ProductDto updateProduct(Long id, ProductRequest request) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        Product product = optionalProduct.orElseThrow(() -> new BusinessException("Product not found"));

        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new BusinessException("Category not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(category);
        product.setOriginalPrice(request.getOriginalPrice());

        return toResponseDto(productRepository.save(product));
    }

    public List<ProductDto> getAll() {
        return productRepository.findAll().stream().map(this::toResponseDto).toList();
    }

    public ProductDto getById(Long id) {
        return toResponseDto(productRepository.findById(id).orElseThrow(() -> new BusinessException("Product not found")));
    }

    public ProductDto deleteById(Long id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            throw new BusinessException("Product not found");
        }

        productRepository.deleteById(id);

        return toResponseDto(product.get());
    }

    private ProductDto toResponseDto(Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getDescription(), product.getPrice(),product.getOriginalPrice(), product.getStockQuantity(), product.getCategory() != null ? product.getCategory().getId() : null);
    }
}
