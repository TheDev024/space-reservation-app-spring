package org.td024.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.td024.enums.WorkspaceType;

import java.math.BigDecimal;

public class CreateWorkspace {
    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Address must not be blank")
    private String address;

    private WorkspaceType type;

    @Min(value = 0, message = "Price must be greater than or equal to zero")
    private BigDecimal price;

    public CreateWorkspace() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public WorkspaceType getType() {
        return type;
    }

    public void setType(WorkspaceType type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
