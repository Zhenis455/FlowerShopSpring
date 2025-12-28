package kz.narxoz.flowershop.dto.product;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class ProductRequest {
    @NotBlank @Size(min = 2, max = 120)
    private String name;

    @Size(max = 800)
    private String description;

    @NotNull
    private Long categoryId;

    @NotNull @DecimalMin(value = "0.01")
    private BigDecimal price;

    @Min(0)
    private int initialStock;

    private String imageUrl;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getInitialStock() { return initialStock; }
    public void setInitialStock(int initialStock) { this.initialStock = initialStock; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
