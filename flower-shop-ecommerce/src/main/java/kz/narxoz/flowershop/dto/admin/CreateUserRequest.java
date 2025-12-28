package kz.narxoz.flowershop.dto.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateUserRequest {
    @Email @NotBlank
    private String email;

    @NotBlank @Size(min = 2, max = 80)
    private String fullName;

    @NotBlank @Size(min = 6, max = 60)
    private String password;

    @NotBlank
    private String role; // ADMIN, USER, MANAGER

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
