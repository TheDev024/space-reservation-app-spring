package org.td024.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import org.td024.entity.Interval;

import java.util.Date;

public class MakeReservation {
    @NotBlank(message = "Name must not be blank")
    private String name;

    @FutureOrPresent(message = "Start Cannot Be In The Past")
    private Date startTime;


    private Date endTime;

    public MakeReservation() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
