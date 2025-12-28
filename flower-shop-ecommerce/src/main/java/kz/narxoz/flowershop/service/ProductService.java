package kz.narxoz.flowershop.service;

import kz.narxoz.flowershop.dto.product.ProductRequest;
import kz.narxoz.flowershop.dto.product.ProductResponse;
import kz.narxoz.flowershop.entity.Inventory;
import kz.narxoz.flowershop.entity.Product;
import kz.narxoz.flowershop.exception.NotFoundException;
import kz.narxoz.flowershop.mapper.ProductMapper;
import kz.narxoz.flowershop.repository.CategoryRepository;
import kz.narxoz.flowershop.repository.InventoryRepository;
import kz.narxoz.flowershop.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductMapper mapper;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          InventoryRepository inventoryRepository,
                          ProductMapper mapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.inventoryRepository = inventoryRepository;
        this.mapper = mapper;
    }

    public List<ProductResponse> listActive(Long categoryId) {
        List<Product> list = (categoryId == null)
                ? productRepository.findByActiveTrue()
                : productRepository.findByCategoryIdAndActiveTrue(categoryId);
        return list.stream().map(mapper::toResponse).toList();
    }

    public ProductResponse get(Long id) {
        Product p = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
        return mapper.toResponse(p);
    }

    @Transactional
    public ProductResponse create(ProductRequest req) {
        var category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        Product p = new Product();
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        p.setCategory(category);
        p.setPrice(req.getPrice());
        p.setImageUrl(req.getImageUrl());
        p.setActive(true);

        p = productRepository.save(p);

        Inventory inv = new Inventory();
        inv.setProduct(p);
        inv.setQuantity(req.getInitialStock());
        inventoryRepository.save(inv);

        p.setInventory(inv);
        return mapper.toResponse(p);
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest req) {
        Product p = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
        var category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        p.setCategory(category);
        p.setPrice(req.getPrice());
        p.setImageUrl(req.getImageUrl());

        var inv = inventoryRepository.findByProductId(p.getId())
                .orElseThrow(() -> new NotFoundException("Inventory not found"));
        inv.setQuantity(req.getInitialStock());

        return mapper.toResponse(p);
    }

    @Transactional
    public void deactivate(Long id) {
        Product p = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
        p.setActive(false);
    }
}
