package org.td024.dto;

import jakarta.validation.constraints.NotBlank;

public class EditReservation {
    @NotBlank(message = "Name must not be blank")
    private String name;

    public EditReservation() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
