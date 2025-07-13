package org.td024.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterDto {
    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "(.*[a-z].*)+", message = "Password must contain at least one lowercase letter")
    @Pattern(regexp = "(.*[A-Z].*)+", message = "Password must contain at least one uppercase letter")
    @Pattern(regexp = "(.*[0-9].*)+", message = "Password must contain at least one digit")
    @Pattern(regexp = "(.*[!@#$%^&*()_+\\-\\[\\]{}:\";',./<>?`~].*)+", message = "Password must contain at least one special character")
    private String password;

    private String repeatPassword;

    public RegisterDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}
