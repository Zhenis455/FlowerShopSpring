package kz.narxoz.flowershop.service;

import kz.narxoz.flowershop.dto.product.ProductRequest;
import kz.narxoz.flowershop.entity.Category;
import kz.narxoz.flowershop.entity.Inventory;
import kz.narxoz.flowershop.entity.Product;
import kz.narxoz.flowershop.exception.NotFoundException;
import kz.narxoz.flowershop.mapper.ProductMapper;
import kz.narxoz.flowershop.repository.CategoryRepository;
import kz.narxoz.flowershop.repository.InventoryRepository;
import kz.narxoz.flowershop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Test
    void createProduct_savesProductAndInventory() {
        ProductRepository productRepo = mock(ProductRepository.class);
        CategoryRepository categoryRepo = mock(CategoryRepository.class);
        InventoryRepository inventoryRepo = mock(InventoryRepository.class);
        ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

        ProductService service = new ProductService(productRepo, categoryRepo, inventoryRepo, mapper);

        Category cat = new Category(); cat.setId(10L); cat.setName("Roses");
        when(categoryRepo.findById(10L)).thenReturn(Optional.of(cat));

        when(productRepo.save(any(Product.class))).thenAnswer(inv -> {
            Product p = inv.getArgument(0);
            p.setId(1L);
            return p;
        });

        ProductRequest req = new ProductRequest();
        req.setName("Red Roses Bouquet");
        req.setDescription("Fresh red roses, 15 pcs");
        req.setCategoryId(10L);
        req.setPrice(new BigDecimal("12990.00"));
        req.setInitialStock(5);
        req.setImageUrl("https://example.com/rose.jpg");

        var resp = service.create(req);

        assertEquals("Red Roses Bouquet", resp.name());
        assertEquals(5, resp.stock());
        verify(inventoryRepo, times(1)).save(any(Inventory.class));
    }

    @Test
    void getProduct_whenNotFound_throws() {
        ProductRepository productRepo = mock(ProductRepository.class);
        CategoryRepository categoryRepo = mock(CategoryRepository.class);
        InventoryRepository inventoryRepo = mock(InventoryRepository.class);
        ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

        ProductService service = new ProductService(productRepo, categoryRepo, inventoryRepo, mapper);

        when(productRepo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.get(999L));
    }
}
