package kz.narxoz.flowershop.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateProfileRequest {
    @NotBlank @Size(min = 2, max = 80)
    private String fullName;

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}
