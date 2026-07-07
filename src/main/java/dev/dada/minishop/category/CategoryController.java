package dev.dada.minishop.category;

import dev.dada.minishop.category.dto.CategoryDto;
import dev.dada.minishop.category.dto.CategoryRequest;
import dev.dada.minishop.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** TASK MS-05: GET /api/categories (public), POST/PUT/DELETE (admin). */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    // TODO MS-05

    @GetMapping("/all")
    public ApiResponse<List<CategoryDto>> getAllCategories() {
        return ApiResponse.ok(categoryService.getAll());
    }

    @PostMapping
    public ApiResponse<CategoryDto> createCategory(@Valid @RequestBody CategoryRequest request) {
        return ApiResponse.ok(categoryService.create(request));
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryDto> getById(@PathVariable Long id) {
        return ApiResponse.ok(categoryService.getById(id));
    }

    @PatchMapping("/{id}")
    public ApiResponse<CategoryDto> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        return ApiResponse.ok(categoryService.update(id, request));
    }
}
