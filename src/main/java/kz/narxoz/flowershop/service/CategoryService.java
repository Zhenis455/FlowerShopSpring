package kz.narxoz.flowershop.service;

import kz.narxoz.flowershop.dto.category.CategoryRequest;
import kz.narxoz.flowershop.dto.category.CategoryResponse;
import kz.narxoz.flowershop.exception.BadRequestException;
import kz.narxoz.flowershop.exception.NotFoundException;
import kz.narxoz.flowershop.mapper.CategoryMapper;
import kz.narxoz.flowershop.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryService(CategoryRepository repository, CategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CategoryResponse> list() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    public CategoryResponse get(Long id) {
        var cat = repository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        return mapper.toResponse(cat);
    }

    @Transactional
    public CategoryResponse create(CategoryRequest req) {
        if (repository.existsByNameIgnoreCase(req.getName())) {
            throw new BadRequestException("Category already exists");
        }
        var entity = mapper.toEntity(req);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest req) {
        var entity = repository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        mapper.update(req, entity);
        return mapper.toResponse(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Category not found");
        }
        repository.deleteById(id);
    }
}
