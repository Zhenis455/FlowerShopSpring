package kz.narxoz.flowershop.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryRequest {
    @NotBlank @Size(min = 2, max = 80)
    private String name;

    @Size(max = 400)
    private String description;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
