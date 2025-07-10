package org.td024.dto;

import jakarta.validation.constraints.NotBlank;
import org.td024.entity.Interval;

public class MakeReservation {
    @NotBlank(message = "Name must not be blank")
    private String name;

    private Interval interval;

    public MakeReservation() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }
}
