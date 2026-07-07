package dev.dada.minishop.category;

import dev.dada.minishop.category.dto.CategoryDto;
import dev.dada.minishop.category.dto.CategoryRequest;
import dev.dada.minishop.exception.BusinessException;
import dev.dada.minishop.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

/**
 * TASK MS-05: CRUD category.
 */
@Service
public class CategoryService {
    // TODO MS-05
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> getAll() {
        return categoryRepository.findAll().stream().map(this::toResponseDto).toList();
    }

    public CategoryDto create(CategoryRequest request) {
        if (categoryRepository.existsBySlug(request.getSlug())) {
            throw new BusinessException("Category slug already exists");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setSlug(request.getSlug());

        return toResponseDto(categoryRepository.save(category));
    }

    public CategoryDto update(@PathVariable Long id, CategoryRequest request) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new ResourceNotFoundException("Category not found");
        }

        if (categoryRepository.existsBySlug(request.getSlug())) {
            throw new BusinessException("Category slug already exists");
        }

        category.get().setName(request.getName());
        category.get().setSlug(request.getSlug());

        return toResponseDto(categoryRepository.save(category.get()));
    }

    public CategoryDto getById(Long id) {
        return toResponseDto(categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found")));
    }

    private CategoryDto toResponseDto(Category category) {
        return new CategoryDto(category.getId(), category.getName(), category.getSlug());
    }
}
